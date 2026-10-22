# Aplikasi Rental Kendaraan — UAS OOP

Proyek kelompok UAS SI204 Object-Oriented Programming.  
Skenario: **Aplikasi Rental Kendaraan** (sewa motor, mobil, dan sepeda listrik).

---

## 🚀 Fitur Utama

1. **Daftar Kendaraan:** Melihat katalog armada kendaraan yang tersedia.
2. **Proses Sewa:** Melakukan peminjaman unit dan validasi ketersediaan.
3. **Proses Pengembalian:** Mengembalikan unit dan memperbarui status sewa.
4. **Hitung Total Biaya:** Kalkulasi biaya fleksibel via Strategy Pattern dan cetak struk.

---

## 🏛️ Persyaratan Wajib UAS

- [x] Enkapsulasi
- [x] Pewarisan (*inheritance*) & *overriding*
- [x] Polimorfisme
- [x] *Abstract class* & *interface*
- [x] Exception handling (input tidak valid tidak menyebabkan aplikasi crash)
- [x] 2 Design Pattern terpilih: **Factory Pattern** & **Strategy Pattern**
- [ ] Diagram kelas UML (tersimpan di `docs/`)
- [x] Database lokal SQLite (zero-config auto-creation & auto-seed)

---

## 📂 Struktur Folder Proyek

```text
vehicle-rental-system/
├── .vscode/          # Pengaturan classpath Java di VS Code
├── bin/              # Output bytecode (.class)
├── data/             # File database SQLite (rental.db)
├── lib/              # Driver JDBC (sqlite-jdbc-3.41.2.2.jar)
├── docs/             # Diagram UML kelas (gambar / PDF)
├── src/              # Source code Java ber-package standar
│   ├── config/       # Konfigurasi koneksi SQLite
│   ├── model/        # Model entitas (Vehicle, Car, Motorcycle, ElectricBike)
│   ├── factory/      # Factory Pattern
│   ├── strategy/     # Strategy Pattern
│   ├── service/      # Layanan bisnis fitur
│   ├── exception/    # Custom exceptions
│   └── Main.java     # Entry point aplikasi
├── run.bat           # Script 1-klik untuk Windows CMD
├── run.ps1           # Script 1-klik untuk PowerShell
├── run.sh            # Script 1-klik untuk Linux / macOS
├── task.md           # Master blueprint & checklist tugas tim
└── README.md
```

---

## 🛠️ Build & Run

### Opsi A: Menggunakan Script Pelari (Paling Praktis)

- **Linux / macOS:**
  ```bash
  ./run.sh
  ```
- **Windows (PowerShell):**
  ```powershell
  .\run.ps1
  ```
- **Windows (CMD):**
  ```cmd
  run.bat
  ```

### Opsi B: Compile Manual via Javac

```powershell
javac -encoding UTF-8 -cp "lib/*;src" -d bin (Get-ChildItem -Recurse -Filter *.java src | ForEach-Object { $_.FullName })
java -cp "bin;lib/*" Main
```

---

## 👥 Tim Pengembang

| Nama                  |      NIM      |     Peran     | Tanggung Jawab Modul                          |
| --------------------- | :-----------: | :-----------: | --------------------------------------------- |
| **Arif Nofriadi**     | `25120410009` | Ketua Tim     | Fitur 4: Total Biaya, Strategy Pattern & Nota |
| **Bayu Putra Efendi** | `25120410005` |    Anggota    | Database SQLite & Fitur 2: Proses Sewa        |
| **Muhammad Fatoni**   | `25120410001` |    Anggota    | Fitur 1: Daftar Kendaraan & Factory Pattern   |
| **Mitha Syafira**     | `25120410010` |    Anggota    | Fitur 3: Proses Pengembalian Kendaraan        |
