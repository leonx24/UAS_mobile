# 🚀 WorkSync - Employee Tasks Management System

WorkSync adalah aplikasi Android manajemen tugas karyawan berbasis peran (*role-based*) yang dirancang khusus untuk bisnis jasa pembuatan website dan aplikasi. Aplikasi ini dikembangkan sebagai proyek tugas **UAS Pemrograman Mobile 1**.

Hierarki data diatur terstruktur secara efisien: **Client ➔ Project ➔ Task**. Aplikasi ini menggunakan desain UI modern bertema gelap (**Premium Glassmorphism**) di halaman login, dipadukan dengan **Header Gradient Melengkung** di seluruh tab navigasi utama untuk menghadirkan pengalaman visual yang bersih, modern, dan profesional.

---

## 👥 Anggota Kelompok (UAS Pemrograman Mobile 1)

Berikut adalah identitas pengembang aplikasi:

| No. | Nama Lengkap | NIM | Kelas | Peran dalam Tim |
| :--- | :--- | :--- | :--- | :--- |
| 1 | **Nama Anggota 1** | `__________` | `__________` | Android Developer |
| 2 | **Nama Anggota 2** | `__________` | `__________` | UI/UX Designer |
| 3 | **Nama Anggota 3** | `__________` | `__________` | QA Engineer |

---

## 🔑 Informasi Akun Login (Demo)

Aplikasi memiliki 2 peran (*role*) utama yang membatasi hak akses fitur pada dashboard dan detail proyek:

| Peran (Role) | Email | Password | Nama | Akses Utama |
| :--- | :--- | :--- | :--- | :--- |
| **Project Manager (PM)** | `pm@worksync.com` | `pm123` | Leon | Hak penuh membuat proyek, mengedit proyek, menghapus proyek, dan membuat tugas. |
| **Karyawan / Staff** | `staff@worksync.com` | `staff123` | Andi | Hak melihat proyek, melihat tugas, memperbarui status tugas, dan melampirkan berkas. |

---

## ✨ Fitur-Fitur Unggulan

*   **🛡️ Role-Based Access Control (RBAC)**
    Membatasi tombol penambahan proyek, penyuntingan, dan pembuatan tugas hanya untuk akun dengan role *Project Manager*.
*   **💾 Database Persisten Lokal (SharedPreferences & Gson)**
    Mengamankan penyimpanan data proyek, tugas, dan histori aktivitas langsung ke memori lokal HP. Data tidak akan hilang saat aplikasi ditutup atau HP dimatikan.
*   **📊 Statistik Dashboard Dinamis**
    Menampilkan visualisasi progress kerja secara *realtime* di dashboard utama (Jumlah Proyek, Tugas Aktif, Tugas Sedang Dikerjakan, dan Tugas Selesai).
*   **📎 File Picker Manager Terintegrasi**
    Membuka File Manager bawaan HP saat mengklik "Attach File" di detail tugas, membolehkan unggah berbagai tipe file (`*/*`), dan menampilkan nama file asli secara dinamis.
*   **🔍 Kolom Pencarian Cerdas & Filter Prioritas**
    Sistem pencarian independen di tab home untuk menyaring tugas berdasarkan kata kunci tertentu, tanpa memengaruhi atau mengosongkan daftar proyek saat berpindah tab.
*   **🌓 Fitur Dark Mode & Light Mode**
    Pengguna dapat beralih antara tema gelap dan terang melalui pengaturan di halaman profil untuk kenyamanan visual yang lebih baik.
*   **📈 Progress Proyek Dinamis**
    Progress persentase pada proyek akan diperbarui secara otomatis ketika status tugas diubah menjadi "Done".
*   **👤 Manajemen Token Sesi (Session Token)**
    Menggunakan `TokenManager` untuk mengenkripsi status login. Sekali login, pengguna tidak perlu mengetik ulang email & password saat aplikasi dibuka kembali.

---

## 📖 Panduan Lengkap Cara Pakai (User Guide)

> [!IMPORTANT]
> Mode demo saat ini berjalan dalam penyimpanan lokal persisten (*offline data memory*). Seluruh perubahan data akan langsung tersimpan di HP masing-masing pengguna.

### 💻 A. Sebagai Project Manager (PM)
1. **Login**: Masuk menggunakan email `pm@worksync.com` dan password `pm123`.
2. **Dashboard & Statistik**: Pantau total proyek aktif dan jumlah tugas yang terbagi berdasarkan status di bagian atas dashboard.
3. **Membuat Proyek Baru**: Klik tombol Floating Action Button (**FAB ➕**) di pojok kanan bawah dashboard, pilih **"New Project"**, isi nama proyek, nama klien, tenggat waktu, dan jumlah anggota, lalu klik **Save Project**.
4. **Melihat & Mengedit Proyek**: Masuk ke tab **Projects**, klik proyek yang Anda buat. Di halaman detail, Anda dapat mengklik tombol **Edit** untuk mengubah data, atau **Delete** untuk menghapus proyek tersebut.
5. **Membuat Tugas Baru**:
    *   **Metode 1**: Klik **FAB ➕** di dashboard, pilih **"New Task"**, isi judul tugas, pilih proyek terkait pada dropdown, pilih tingkat prioritas, isi deskripsi tugas, lalu klik **Create Task**.
    *   **Metode 2 (Rekomendasi)**: Buka detail proyek yang sudah ada, klik tombol **+ ADD TASK** di sebelah daftar Tasks. Dropdown nama proyek akan terisi secara otomatis.

---

### 🏃 B. Sebagai Karyawan / Staff
1. **Login**: Masuk menggunakan email `staff@worksync.com` dan password `staff123`.
2. **Melihat Tugas**: Lihat tugas yang ditugaskan kepada Anda hari ini di bagian **Today's Tasks** pada dashboard utama, atau masuk ke tab **Tasks** di bagian bawah.
3. **Mengubah Status Tugas**: 
    *   Klik kartu tugas yang ingin Anda kerjakan untuk masuk ke halaman **Task Detail**.
    *   Pilih status pengerjaan baru pada pilihan toggle group: **To Do** (Belum dikerjakan), **Doing** (Sedang dikerjakan), atau **Done** (Selesai).
4. **Melampirkan Berkas Kerja**: 
    *   Di halaman detail tugas, klik **Attach File**.
    *   Aplikasi akan otomatis membuka File Manager bawaan HP Anda.
    *   Pilih berkas hasil kerja Anda (PDF, Gambar, Zip, dll.). Nama file yang dipilih akan langsung tertera di bawah tombol lampiran.
5. **Menyimpan Progress**: Klik tombol **SUBMIT TASK** di bagian bawah. Status tugas akan diperbarui secara permanen dan memicu notifikasi sistem.

---

## 🛠️ Panduan Build & Running (Android Studio)

### Prasyarat System:
*   Android Studio Jellyfish / Koala (atau versi terbaru)
*   Android SDK Level 34 (Android 14)
*   JDK 17

### Langkah Menjalankan Aplikasi:
1.  Buka folder proyek `WorkSyncc` di Android Studio Anda.
2.  Lakukan sinkronisasi Gradle (klik tombol **Sync Project with Gradle Files** di pojok kanan atas).
3.  Aktifkan **USB Debugging** di menu Opsi Pengembang pada HP Android asli Anda, lalu hubungkan HP ke PC menggunakan kabel data.
4.  Pilih HP Anda pada daftar perangkat di Android Studio, lalu klik tombol **Run** (ikon segitiga hijau ⏵) atau tekan **`Shift + F10`**.

### Langkah Membangun File APK (`.apk`):
1.  Klik menu **Build** pada toolbar atas Android Studio.
2.  Pilih **Build Bundle(s) / APK(s)** ➔ **Build APK(s)**.
3.  Tunggu proses kompilasi Gradle selesai.
4.  Setelah selesai, klik pop-up **locate** di pojok kanan bawah untuk membuka folder file APK (`app-debug.apk`) Anda yang siap dipindahkan dan diinstal di HP.
