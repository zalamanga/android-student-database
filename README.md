# Android Student Database

Aplikasi latihan Android Java untuk menyimpan data siswa di SQLite pada satu perangkat. Alurnya: buat akun lokal, masuk, buka **Data Siswa**, lalu tambah, ubah, atau hapus catatan. Nama aplikasi dan susunan layar dasar dari proyek AplikasiDatabase dipertahankan.

## Isi aplikasi

- Akun demo lokal dengan username unik dan password 8 sampai 128 karakter untuk pendaftaran baru.
- Daftar siswa berisi kelas, nama, jenis kelamin, dan tanggal lahir.
- Date picker serta validasi tanggal kalender dan tanggal lahir yang tidak berada di masa depan.
- Tambah melalui menu **Add Record**; ketuk satu baris daftar untuk membuka **Update/Delete**.
- Logout menghapus sesi lokal dan membersihkan halaman sebelumnya dari back stack.

Tidak ada server, Firebase, akun internet, atau sinkronisasi. Catatan siswa dibagi oleh semua akun demo pada instalasi yang sama, bukan dipisahkan per akun.

## Buka dan build

Gunakan Android Studio dengan JDK 17 dan Android SDK Platform 35. Pilih folder repo ini sebagai proyek Gradle. Android Studio membuat `local.properties` sesuai lokasi SDK komputer sendiri; file tersebut tidak disertakan.

| Pengaturan | Versi |
| --- | --- |
| Android Gradle Plugin | 8.8.2 |
| Gradle Wrapper | 8.10.2, checksum distribusi dipin |
| compileSdk / targetSdk | 35 / 34 |
| minSdk | 24, Android 7.0 |
| Bahasa source | Java dengan kompatibilitas Java 8 |

Setelah SDK dan dependency tersedia, dari terminal di root repo:

```powershell
.\gradlew.bat testDebugUnitTest assembleDebug
```

APK debug berada di `app/build/outputs/apk/debug/`. Untuk mencoba UI, jalankan konfigurasi `app` pada emulator atau perangkat Android API 24 ke atas, lalu gunakan nama dan catatan sintetis. Tidak ada kredensial bawaan.

## Penyimpanan dan password lama

DatabaseHelper membuat tabel akun dan siswa pada database privat aplikasi. Password baru disimpan sebagai PBKDF2 dengan salt acak. Password plaintext dari versi latihan lama hanya diganti dengan hash setelah password yang dimasukkan cocok dan pembaruan satu baris berhasil. Password salah tidak mengubah data. Versi database tetap 1; tidak ada perintah DROP atau reset tabel.

Database lama dapat berisi username ganda karena source awal belum memberi batas unik. Login menolak nama yang ambigu, tanpa menebak atau menghapus akun. Penyelesaian data ganda memerlukan pemeriksaan dan cadangan sendiri. Akun kosong dari versi lama tidak dibuat ulang otomatis.

Ini autentikasi lokal untuk pembelajaran. Sesi hanya disimpan pada perangkat; tidak ada verifikasi identitas oleh server, pemulihan akun, pemisahan peran, atau perlindungan terhadap orang yang mengendalikan data aplikasi. Hash lokal tidak membuat aplikasi siap digunakan sebagai sistem akun produksi. Backup otomatis dinonaktifkan melalui manifest.

## Peta kode

| Berkas di `app/src/main/java/com/rapik/example/aplikasidatabase/` | Tanggung jawab |
| --- | --- |
| LoginActivity, RegisterActivity, MainActivity | Akun lokal, sesi, dan navigasi dashboard |
| DataMemberActivity | Membaca Cursor dan menampilkan daftar |
| AddMemberActivity, ModifyMemberActivity | Validasi form serta tambah/ubah/hapus |
| config/DatabaseHelper, config/UserModel | Skema SQLite dan penyimpanan akun |
| model/MemberModel | Query catatan siswa |
| config/PasswordHasher, config/MemberInput, config/Session | Hash/migrasi password, aturan input, dan gerbang sesi lokal |

## Status pemeriksaan

Pada 27 Oktober 2026, `assembleDebug`, `testDebugUnitTest`, dan `lintDebug` berhasil dijalankan dengan JDK 17 serta SDK 35. LocalRulesTest menguji password benar/salah, salt, migrasi plaintext yang cocok, input hash rusak, serta tanggal dan isian yang tidak valid. Rincian jumlah tes dan warning lint ada di [hasil pemeriksaan](BUILD-VERIFICATION.md).

UI, database SQLite pada perangkat, dan screenshot belum diuji pada emulator atau HP. Unit test tidak menggantikan pemeriksaan alur CRUD langsung.

## Asal source

Berasal dari proyek lokal AplikasiDatabase, dengan namespace aslinya dipertahankan. Tidak ditemukan lisensi proyek atau URL upstream yang dapat dipastikan; tidak ditambahkan klaim kepemilikan baru. Header lisensi Gradle Wrapper tetap berlaku untuk berkas wrapper, bukan otomatis untuk seluruh aplikasi. Cache, hasil build, database pengguna, konfigurasi SDK lokal, dan kunci penandatanganan tidak masuk repo.
