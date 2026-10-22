# 📋 Blueprint & Pembagian Tugas Proyek UAS OOP

## Sistem Rental Kendaraan (Java Console Application)

**Mata Kuliah:** SI204 Object-Oriented Programming  
**Program Studi:** Sistem Informasi — Universitas Cakrawala  

---

## 👥 Profil Tim & Pembagian Modul Utama

| No  | Nama                 |   NIM / ID    |   Peran   | Modul / Tanggung Jawab Utama                                                                                                          |
| :-: | -------------------- | :-----------: | :-------: | ------------------------------------------------------------------------------------------------------------------------------------- |
|  1  | **Arif Nofriadi**    | `25120410009` | Ketua Tim | **Fitur 4: Strategi Biaya & Cetak Struk** (`strategy/*`, `service/CostCalculatorService.java`, integrasi menu utama)                |
|  2  | **Bayu Putra Efendi**| `25120410005` |  Anggota  | **Arsitektur Database SQLite & Fitur 2: Proses Sewa** (`config/DatabaseConnection.java`, `model/RentalTransaction.java`, `service/RentalService.java`) |
|  3  | **Muhammad Fatoni**  | `25120410001` |  Anggota  | **Fitur 1: Manajemen Kendaraan** (`model/*`, `factory/VehicleFactory.java`, `service/VehicleService.java`)                             |
|  4  | **Mitha Syafira**    | `25120410010` |  Anggota  | **Fitur 3: Proses Pengembalian Kendaraan** (`service/ReturnService.java`)                                                            |

---

## 🏛️ Pemenuhan Standar & Rubrik Penilaian UAS

| Kriteria Penilaian            | Implementasi Arsitektural dalam Kode                                                                                                           |
| ----------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------- |
| **1. Enkapsulasi**            | Semua variabel atribut bersifat `private` atau `protected`. Akses data hanya melalui getter/setter dengan validasi nilai.                      |
| **2. Pewarisan & Overriding** | Superclass abstrak `Vehicle` diwarisi oleh `Car`, `Motorcycle`, dan `ElectricBike`. Method `getType()` dan `getBaseDailyRate()` di-override. |
| **3. Polimorfisme**           | Koleksi kendaraan dikelola sebagai `List<Vehicle>`. Pemanggilan `calculateRentalCost()` berjalan dinamis sesuai objek konkret.                |
| **4. Abstraksi**              | Class abstrak `model.Vehicle` dan Interface `strategy.PricingStrategy`.                                                                        |
| **5. Pattern 1 (Factory)**    | `factory.VehicleFactory` mengkapsulasi pembuatan objek turunan `Vehicle` berdasarkan string tipe kendaraan.                                   |
| **6. Pattern 2 (Strategy)**   | `strategy.PricingStrategy` diimplementasikan oleh `RegularPricingStrategy` dan `DiscountPricingStrategy` untuk kalkulasi biaya sewa.          |
| **7. Exception Handling**     | Custom exception (`VehicleNotFoundException`, `RentalValidationException`) dan proteksi input console (`Scanner`) agar program tidak crash.    |
| **8. Database (SQLite)**      | Database lokal `data/rental.db` via `lib/sqlite-jdbc-3.41.2.2.jar` yang otomatis membuat tabel & data awal (*auto-seed*) saat dijalankan.   |

---

## 📂 Struktur Standar Direktori Java (Service-Oriented Architecture)

Proyek ini menggunakan pembagian paket (*package*) standar Java yang modular agar setiap anggota tim memiliki file tugas mandiri tanpa risiko *merge conflict* di Git:

```text
vehicle-rental-system/
├── .vscode/
│   └── settings.json                 # Konfigurasi classpath Java di VS Code
├── bin/                              # Folder hasil compile bytecode (.class)
├── data/
│   └── rental.db                     # Database SQLite lokal (otomatis dibuat & di-ignore git)
├── lib/
│   ├── README.md                     # Panduan driver SQLite JDBC
│   └── sqlite-jdbc-3.41.2.2.jar      # Driver SQLite JDBC (standalone zero-dependency)
├── docs/
│   └── ClassDiagram.png              # Diagram kelas UML
├── src/
│   ├── Main.java                     # Entry Point & Menu Utama Interaktif (Console)
│   │
│   ├── config/                       # [Bayu]: Konfigurasi Database
│   │   └── DatabaseConnection.java   # Helper koneksi SQLite, auto-folder, auto-tables & seed data
│   │
│   ├── model/                        # Pilar OOP: Model & Enkapsulasi
│   │   ├── Vehicle.java              # [Abstract Class] Superclass kendaraan
│   │   ├── Car.java                  # [Subclass] Mobil (Tarif Rp250.000/hari)
│   │   ├── Motorcycle.java           # [Subclass] Motor (Tarif Rp75.000/hari)
│   │   ├── ElectricBike.java         # [Subclass] Sepeda Listrik (Tarif Rp50.000/hari)
│   │   └── RentalTransaction.java    # [Bayu]: Model data entitas sewa & pengembalian
│   │
│   ├── factory/                      # [Fatoni]: [Pattern 1: Factory Pattern]
│   │   └── VehicleFactory.java       # Penghasil objek Vehicle secara aman
│   │
│   ├── strategy/                     # [Arif]: [Pattern 2: Strategy Pattern]
│   │   ├── PricingStrategy.java      # [Interface] Kontrak kalkulasi tarif
│   │   ├── RegularPricingStrategy.java   # Tarif standar (Base Rate × Hari)
│   │   └── DiscountPricingStrategy.java  # Diskon 10% jika sewa ≥ 3 hari
│   │
│   ├── service/                      # Logika Bisnis (Dibagi 1 Mahasiswa 1 Service)
│   │   ├── VehicleService.java       # [Fitur 1 - Fatoni]: Pengelolaan armada
│   │   ├── RentalService.java        # [Fitur 2 - Bayu]: Transaksi penyewaan
│   │   ├── ReturnService.java        # [Fitur 3 - Mitha]: Transaksi pengembalian
│   │   └── CostCalculatorService.java# [Fitur 4 - Arif]: Perhitungan biaya & cetak nota
│   │
│   └── exception/                    # Exception Handling
│       ├── VehicleNotFoundException.java
│       └── RentalValidationException.java
│
├── run.bat                           # Script 1-klik untuk compile & run di CMD/Windows
├── run.ps1                           # Script 1-klik untuk compile & run di PowerShell
├── task.md                           # Master blueprint & checklist tugas tim
└── README.md                         # Dokumentasi umum repositori
```

---

## 📝 Rincian Spesifikasi & Checklist Tugas per Mahasiswa

---

### 1️⃣ Muhammad Fatoni (`25120410001`)

**Fokus:** *Fitur 1 — Manajemen & Katalog Kendaraan*  
**Lokasi File Kerja:** `src/model/*`, `src/factory/VehicleFactory.java`, `src/service/VehicleService.java`

- [x] Merapikan class model (`Vehicle.java`, `Car.java`, `Motorcycle.java`, `ElectricBike.java`) ke dalam package `model`.
- [x] Merapikan `VehicleFactory.java` ke dalam package `factory`.
- [ ] Membuat class `service/VehicleService.java` dengan method:

```java
public class VehicleService {
    // 1. Menampilkan semua kendaraan ke layar
    public void displayAllVehicles();

    // 2. Menampilkan hanya kendaraan yang berstatus available (siap sewa)
    public void displayAvailableVehicles();

    // 3. Mengambil objek kendaraan berdasarkan ID (misal: "CAR-001")
    public Vehicle getVehicleById(String id) throws VehicleNotFoundException;

    // 4. Menambah kendaraan baru via VehicleFactory lalu simpan ke DB
    public void addVehicle(String type, String id, String brand, String model, String plateNumber);
}
```

---

### 2️⃣ Bayu Putra Efendi (`25120410005`)

**Fokus:** *Inisialisasi Database SQLite & Fitur 2 — Proses Peminjaman / Sewa*  
**Lokasi File Kerja:** `src/config/DatabaseConnection.java`, `src/model/RentalTransaction.java`, `src/service/RentalService.java`

#### A. Database SQLite & Konfigurasi (Selesai):

- [x] Membuat class konfigurasi `config/DatabaseConnection.java`.
- [x] Menyiapkan manajemen path folder khusus `data/rental.db` (auto-folder creation).
- [x] Menyiapkan auto-create tabel (`vehicles` dan `rentals`).
- [x] Menyiapkan data awal armada (*seed initial data*) agar aplikasi langsung berisi data saat pertama dijalankan.

#### B. Fitur 2 — Proses Sewa Kendaraan:

- [ ] Membuat model `model/RentalTransaction.java`:
  - Atribut: `rentalId`, `customerName`, `customerPhone`, `vehicleId`, `rentalDays`, `totalCost`, `status` (`"DISEWA"` / `"SELESAI"`), `rentalDate`.
  - Constructor, Getter & Setter lengkap.
- [ ] Membuat class `service/RentalService.java` dengan method:

```java
public class RentalService {
    // Melakukan proses sewa kendaraan
    public RentalTransaction rentVehicle(
        String vehicleId,
        String customerName,
        String phone,
        int days,
        CostCalculatorService costService
    ) throws VehicleNotFoundException, RentalValidationException;
}
```

- [ ] Aturan Validasi Sewa:
  - Durasi hari harus `> 0`.
  - Cek ketersediaan: Jika kendaraan `available == false`, lempar `RentalValidationException("Kendaraan sedang disewa!")`.
  - Ubah status kendaraan: `vehicle.setAvailable(false)`.
  - Simpan riwayat sewa ke tabel database `rentals` dan update status di tabel `vehicles`.

---

### 3️⃣ Mitha Syafira (`2512041010`)

**Fokus:** *Fitur 3 — Proses Pengembalian Kendaraan*  
**Lokasi File Kerja:** `src/service/ReturnService.java`

- [ ] Membuat class `service/ReturnService.java` dengan method:

```java
public class ReturnService {
    // Memproses pengembalian kendaraan berdasarkan ID Transaksi / ID Kendaraan
    public void processReturn(String rentalIdOrVehicleId)
        throws RentalValidationException, VehicleNotFoundException;

    // Menampilkan riwayat transaksi sewa yang sedang aktif
    public void displayActiveRentals();
}
```

- [ ] Aturan Validasi Pengembalian:
  - Cek apakah transaksi sewa berstatus `"DISEWA"`.
  - Ubah status kendaraan menjadi `available = true` kembali.
  - Perbarui status transaksi sewa di database menjadi `"SELESAI"`.
  - Tampilkan pesan konfirmasi sukses pengembalian unit kendaraan.

---

### 4️⃣ Arif Nofriadi (`25120410009` - Ketua Tim)

**Fokus:** *Fitur 4 — Desain Strategy Biaya & Cetak Struk Transaksi*  
**Lokasi File Kerja:** `src/strategy/*`, `src/service/CostCalculatorService.java`, integrasi `src/Main.java`

- [ ] Mengimplementasikan Strategy Pattern:
  - `strategy/RegularPricingStrategy.java`:

```java
// Biaya normal = tarif dasar x hari
public double calculate(double baseRate, int days) {
    return baseRate * days;
}
```

  - `strategy/DiscountPricingStrategy.java`:

```java
// Diskon 10% jika lama sewa >= 3 hari
public double calculate(double baseRate, int days) {
    double total = baseRate * days;
    return (days >= 3) ? total * 0.90 : total;
}
```

- [ ] Membuat class `service/CostCalculatorService.java`:
  - `calculateCost(Vehicle vehicle, int days)`: Menghitung total dengan memilih strategi yang sesuai.
  - `printReceipt(RentalTransaction trx, Vehicle vehicle)`: Mencetak format struk formal yang rapi ke console terminal.
- [ ] Mengoordinasikan integrasi alur menu di [`src/Main.java`](file:///c:/Users/bayup/Documents/Cakrawala%20University/Cakrawala%20University/OOP/vehicle-rental-system/src/Main.java).

---

## 🗄️ Desain Basis Data SQLite (`data/rental.db`)

Setup SQLite dirancang otomatis (*zero config*). Class [`DatabaseConnection.java`](file:///c:/Users/bayup/Documents/Cakrawala%20University/Cakrawala%20University/OOP/vehicle-rental-system/src/config/DatabaseConnection.java) mengelola tabel berikut:

```sql
-- 1. Tabel Kendaraan
CREATE TABLE IF NOT EXISTS vehicles (
    id TEXT PRIMARY KEY,
    type TEXT NOT NULL,
    brand TEXT NOT NULL,
    model TEXT NOT NULL,
    plate_number TEXT NOT NULL UNIQUE,
    base_rate REAL NOT NULL,
    is_available INTEGER NOT NULL DEFAULT 1
);

-- 2. Tabel Transaksi Sewa
CREATE TABLE IF NOT EXISTS rentals (
    rental_id TEXT PRIMARY KEY,
    customer_name TEXT NOT NULL,
    customer_phone TEXT NOT NULL,
    vehicle_id TEXT NOT NULL,
    rental_days INTEGER NOT NULL,
    total_cost REAL NOT NULL,
    status TEXT NOT NULL DEFAULT 'DISEWA',
    created_at TEXT NOT NULL,
    FOREIGN KEY (vehicle_id) REFERENCES vehicles(id)
);
```

---

## 🛠️ Panduan Build, Compile, dan Run

### Cara 1: Menggunakan Script Otomatis (Direkomendasikan)

- **PowerShell:**

```powershell
.\run.ps1
```

- **Command Prompt (CMD):**

```cmd
run.bat
```

### Cara 2: Menggunakan VS Code

1. Buka folder `vehicle-rental-system` di VS Code.
2. Buka file [`src/Main.java`](file:///c:/Users/bayup/Documents/Cakrawala%20University/Cakrawala%20University/OOP/vehicle-rental-system/src/Main.java).
3. Klik tombol **Run Java** (ikon segitiga di pojok kanan atas) atau tekan tombol `F5`.

### Cara 3: Menggunakan Perintah Manual

```powershell
# 1. Compile semua file java ke folder bin
javac -encoding UTF-8 -cp "lib/*;src" -d bin (Get-ChildItem -Recurse -Filter *.java src | ForEach-Object { $_.FullName })

# 2. Jalankan Main
java -cp "bin;lib/*" Main
```
