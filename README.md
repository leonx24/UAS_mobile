<div align="center">

# 🚀 WorkSync
### Employee Tasks Management System

**Aplikasi manajemen tugas karyawan berbasis peran (Role-Based) untuk bisnis jasa pembuatan website & aplikasi**

![Platform](https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Language](https://img.shields.io/badge/Language-Kotlin%2FJava-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![SDK](https://img.shields.io/badge/Min%20SDK-34-blue?style=for-the-badge)
![Status](https://img.shields.io/badge/Status-UAS%20Project-orange?style=for-the-badge)

<br/>

*Dikembangkan sebagai proyek Ujian Akhir Semester — Pemrograman Mobile 1*

</div>

---

## 📌 Tentang Aplikasi

**WorkSync** adalah aplikasi Android manajemen tugas karyawan berbasis peran (*role-based*) yang dirancang khusus untuk bisnis jasa pembuatan website dan aplikasi. Hierarki data diatur secara terstruktur dan efisien:

<div align="center">

**`Client`**  ➔  **`Project`**  ➔  **`Task`**

</div>

Aplikasi ini menghadirkan desain UI modern bertema gelap (**Premium Glassmorphism**) di halaman login, dipadukan dengan **Header Gradient Melengkung** di seluruh tab navigasi utama — menghasilkan pengalaman visual yang bersih, modern, dan profesional.

---

## 👥 Anggota Kelompok

<div align="center">

| No. | Nama Lengkap | NIM | Kelas | Peran dalam Tim |
| :---: | :--- | :---: | :---: | :--- |
| 1 | **Affari Rizky F** | `24552011205` | `TIF RP 24C CID` | 📱 Android Developer |
| 2 | **Gholib Ahmad Refan** | `24552011164` | `TIF RP24C CID` | 🎨 UI/UX Designer |
| 3 | **Nama Anggota 3** | `__________` | `__________` | 🧪 QA Engineer |

</div>

---

## 🔑 Akun Demo (Login)

Aplikasi memiliki **2 peran utama** yang membatasi hak akses fitur pada dashboard dan detail proyek.

<table width="100%">
<tr>
<td width="50%" valign="top">

### 💼 Project Manager
**Nama:** Leon

| Field | Value |
|---|---|
| 📧 Email | `pm@worksync.com` |
| 🔒 Password | `pm123` |

**Hak akses:**
- ✅ Membuat proyek
- ✅ Mengedit proyek
- ✅ Menghapus proyek
- ✅ Membuat tugas

</td>
<td width="50%" valign="top">

### 👷 Karyawan / Staff
**Nama:** Andi

| Field | Value |
|---|---|
| 📧 Email | `staff@worksync.com` |
| 🔒 Password | `staff123` |

**Hak akses:**
- ✅ Melihat proyek
- ✅ Melihat tugas
- ✅ Memperbarui status tugas
- ✅ Melampirkan berkas

</td>
</tr>
</table>

> [!IMPORTANT]
> Mode demo saat ini berjalan dalam penyimpanan lokal persisten (*offline data memory*). Seluruh perubahan data akan langsung tersimpan di HP masing-masing pengguna.

---

## ✨ Fitur Unggulan

<div align="center">

| Fitur | Deskripsi |
| :--- | :--- |
| 🛡️ **Role-Based Access Control** | Membatasi tombol tambah proyek, edit, dan buat tugas hanya untuk akun *Project Manager*. |
| 💾 **Database Persisten Lokal** | Menyimpan data proyek, tugas, dan histori aktivitas langsung ke memori lokal HP menggunakan `SharedPreferences` & `Gson`. Data tidak hilang saat aplikasi ditutup. |
| 📊 **Statistik Dashboard Dinamis** | Visualisasi progress kerja *realtime*: jumlah proyek, tugas aktif, sedang dikerjakan, dan selesai. |
|**🌓 Fitur Dark Mode & Light Mode** |Pengguna dapat beralih antara tema gelap dan terang melalui pengaturan di halaman profil untuk kenyamanan visual yang lebih baik.|
| 📎 **File Picker Manager Terintegrasi** | Membuka File Manager bawaan HP untuk mengunggah berbagai tipe file (`*/*`) di detail tugas. |
| 🔍 **Kolom Pencarian & Filter Prioritas** | Sistem pencarian independen di tab home yang menyaring tugas berdasarkan kata kunci tanpa memengaruhi daftar proyek. |
| 👤 **Manajemen Token Sesi** | `TokenManager` mengenkripsi status login — sekali login, tidak perlu mengetik ulang kredensial. |

</div>

---

## 📖 Panduan Penggunaan

<details open>
<summary><b>💻 A. Sebagai Project Manager (PM)</b></summary>
<br/>

1. **Login** — masuk menggunakan email `pm@worksync.com` dan password `pm123`.
2. **Dashboard & Statistik** — pantau total proyek aktif dan jumlah tugas berdasarkan status di bagian atas dashboard.
3. **Membuat Proyek Baru** — klik tombol **FAB ➕** di pojok kanan bawah dashboard → pilih **"New Project"** → isi nama proyek, nama klien, tenggat waktu, jumlah anggota → klik **Save Project**.
4. **Melihat & Mengedit Proyek** — masuk ke tab **Projects**, klik proyek yang dibuat. Di halaman detail, klik **Edit** untuk mengubah data atau **Delete** untuk menghapus.
5. **Membuat Tugas Baru**:
   - **Metode 1:** klik **FAB ➕** di dashboard → **"New Task"** → isi judul, pilih proyek, prioritas, deskripsi → **Create Task**.
   - **Metode 2 (Rekomendasi):** buka detail proyek → klik **+ ADD TASK** — dropdown proyek otomatis terisi.

</details>

<details open>
<summary><b>🏃 B. Sebagai Karyawan / Staff</b></summary>
<br/>

1. **Login** — masuk menggunakan email `staff@worksync.com` dan password `staff123`.
2. **Melihat Tugas** — cek **Today's Tasks** di dashboard utama, atau buka tab **Tasks**.
3. **Mengubah Status Tugas** — klik kartu tugas → halaman **Task Detail** → pilih status baru: **To Do**, **Doing**, atau **Done**.
4. **Melampirkan Berkas Kerja** — klik **Attach File** → File Manager HP terbuka otomatis → pilih berkas (PDF, gambar, zip, dll).
5. **Menyimpan Progress** — klik **SUBMIT TASK**. Status akan diperbarui secara permanen dan memicu notifikasi sistem.

</details>

---

## 🛠️ Build & Running (Android Studio)

### Prasyarat Sistem

| Requirement | Versi |
| :--- | :--- |
| Android Studio | Jellyfish / Koala (atau lebih baru) |
| Android SDK | Level 34 (Android 14) |
| JDK | 17 |

### ▶️ Menjalankan Aplikasi

```bash
1. Buka folder proyek "WorkSyncc" di Android Studio
2. Klik "Sync Project with Gradle Files" (pojok kanan atas)
3. Aktifkan USB Debugging di Opsi Pengembang HP Android
4. Hubungkan HP ke PC via kabel data
5. Pilih perangkat di daftar device → klik Run ⏵  (atau Shift + F10)
```

### 📦 Membangun File APK

```bash
1. Klik menu "Build" pada toolbar
2. Pilih "Build Bundle(s) / APK(s)" → "Build APK(s)"
3. Tunggu proses kompilasi Gradle selesai
4. Klik notifikasi "locate" untuk membuka folder app-debug.apk
```

---

<div align="center">

### 🏗️ Arsitektur Data

```
Client  ──▶  Project  ──▶  Task
```

<br/>

Made with ❤️ for **Pemrograman Mobile 1** — UAS Project

</div>
