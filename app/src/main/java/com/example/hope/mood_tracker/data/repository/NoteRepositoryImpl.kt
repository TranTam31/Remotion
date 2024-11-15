package com.example.hope.mood_tracker.data.repository

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.hope.auth.ui.sign_in.GoogleAuthUiClient
import com.example.hope.auth.ui.sign_in.UserData
import com.example.hope.mood_tracker.data.database.Note
import com.example.hope.mood_tracker.data.database.NoteDao
import com.example.hope.mood_tracker.utils.NetworkUtils
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.concurrent.ConcurrentLinkedQueue

class NoteRepositoryImpl(
    private val noteDao: NoteDao,
    override val googleAuthUiClient: GoogleAuthUiClient,
    private val context: Context
) : NoteRepository {

    private val offlineQueue: ConcurrentLinkedQueue<OfflineAction> = ConcurrentLinkedQueue()

    // Định nghĩa OfflineAction để lưu thông tin thao tác
    data class OfflineAction(
        val actionType: String, // "ADD", "UPDATE", "DELETE"
        val note: Note
    )


    // chỗ này cập nhật so với code cũ, important nhá
    override val userData: UserData
        get() = googleAuthUiClient.getCurrentUser()


    private val firestore = FirebaseFirestore.getInstance()

    override fun getAllNotes(): Flow<List<Note>> {
        Log.d("UserData trong Repo", "${userData}")
        return noteDao.getNotes(userId = userData.userId)
    }

    override suspend fun insertNote(note: Note) {
        // Lưu ghi chú vào Room
        noteDao.insertNote(note)

        // Kiểm tra kết nối internet và đồng bộ lên Firestore nếu có kết nối
        if (NetworkUtils.isNetworkAvailable(context = context)) {
            syncNoteToFirestore(note) // Sử dụng bản ghi đã có id hợp lệ
        } else {
            offlineQueue.add(OfflineAction("ADD", note))
        }
    }

    override suspend fun updateNote(note: Note) {
        noteDao.updateNote(note)

        if (NetworkUtils.isNetworkAvailable(context = context)) {
            syncNoteToFirestore(note)
        } else {
            offlineQueue.add(OfflineAction("UPDATE", note))
        }
    }

    override suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note)
        // phải thêm logic là xóa note nào nhé
        if (NetworkUtils.isNetworkAvailable(context = context)) {
            firestore.collection("notes").document().delete()
        } else {
            offlineQueue.add(OfflineAction("DELETE", note))
        }
    }

    // Đồng bộ ghi chú từ Room lên Firestore
    private suspend fun syncNoteToFirestore(note: Note) {
        try {
            val noteMap = mapOf(
                "userId" to note.userId,
                "date" to note.date.toString(),
                "content" to note.content,
                "emotion" to note.emotion
            )

            firestore.collection("notes")
                .document()
                .set(noteMap)
                .await() // Sử dụng await để chờ Firebase trả về kết quả
        } catch (e: Exception) {
            // Xử lý lỗi nếu có
            e.printStackTrace()
        }
    }

    // Đồng bộ ghi chú từ Firestore về Room (chỉ lấy ghi chú của userId hiện tại)
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun syncNotesFromFirestore() {
        try {
            val querySnapshot = firestore.collection("notes")
                .whereEqualTo("userId", userData.userId)
                .get()
                .await()
            // Lấy danh sách ghi chú từ Firestore mà không bao gồm `id`
            val notes = querySnapshot.documents.mapNotNull { document ->
                val userId = document.getString("userId") ?: return@mapNotNull null
                val dateStr  = document.getString("date") ?: return@mapNotNull null
                val content = document.getString("content") ?: ""
                val emotion = document.getLong("emotion")?.toInt() ?: 0

                val date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                Log.d("FirestoreData", "Document ID: ${document.id}, userId: $userId, date: $date, content: $content, emotion: $emotion")
                // Tạo một `Note` mới mà không có `id`
                Note(
                    userId = userId,
                    date = date,
                    content = content,
                    emotion = emotion
                )
            }
            Log.d("SyncFunction", "Số lượng notes đã tạo từ Firestore: ${notes.size}")
            // Thêm ghi chú vào Room
            for (note in notes) {
                noteDao.insertNote(note)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Xóa ghi chú từ Firestore
//    private suspend fun deleteNoteFromFirestore(note: Note) {
//        try {
//            firestore.collection("notes")
////                .document(note.id.toString())
//                .document()
//                .delete()
//                .await()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
    override suspend fun syncOfflineQueue() {
        while (offlineQueue.isNotEmpty()) {
            val action = offlineQueue.poll() // Lấy và xóa phần tử đầu tiên trong hàng đợi

            action?.let {
                when (it.actionType) {
                    "ADD" -> syncNoteToFirestore(it.note)
                    "UPDATE" -> firestore.collection("notes").document().set(it.note)
                    "DELETE" -> firestore.collection("notes").document().delete()
                    else -> {}
                }
            }
        }
        Log.d("SyncOff", "Hoạt động")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun clearRoomDataForNewUser() {
        Log.d("ClearFunc", "Nó có hoạt động không?")
        noteDao.deleteAllNotes()  // Xóa tất cả ghi chú trong Room
        syncNotesFromFirestore()  // Đồng bộ lại dữ liệu từ Firestore
        offlineQueue.clear()
    }
}