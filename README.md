# แอปบันทึกรายรับ-รายจ่าย (Expense Tracker)

แอป Android เขียนด้วย **Kotlin + Jetpack Compose** สำหรับบันทึกรายรับ-รายจ่ายส่วนตัว
รองรับ Android 10 (API 29) ขึ้นไป

## ฟีเจอร์หลัก

1. **Dashboard** — สรุปยอดรายรับ/รายจ่าย/คงเหลือของเดือนนี้ + รายการล่าสุด
2. **เพิ่ม/แก้ไขรายการ** — รายรับ/รายจ่าย, จำนวนเงิน, หมวดหมู่, วันที่, เวลา, หมายเหตุ
3. **จัดการหมวดหมู่** — เพิ่ม/แก้ไข/ลบ พร้อมเลือกไอคอนและสี, มีหมวดหมู่เริ่มต้นให้
4. **รายการทั้งหมด** — แบบ Timeline เรียงใหม่→เก่า, ลบด้วยการปัด (มี Undo)
5. **รายละเอียดรายการ** — ดู/แก้ไข/ลบ
6. **ค้นหา** — ค้นจากหมายเหตุ/หมวดหมู่/จำนวนเงิน พร้อม filter รายรับ-รายจ่าย/วันที่
7. **ปฏิทิน** — ดูรายการและยอดรวมรายวันแบบปฏิทินรายเดือน
8. **สถิติ** — Donut chart สัดส่วนรายจ่าย/รายรับตามหมวดหมู่ (วาดด้วย Compose Canvas ไม่พึ่งไลบรารีนอก)
9. **ตั้งค่า** — ธีม Light/Dark/System, สกุลเงิน, เปิด/ปิดเสียง-การสั่น, สำรอง/กู้คืนข้อมูลเป็นไฟล์ CSV

## สถาปัตยกรรม

```
app/src/main/java/com/expensetracker/app/
 ├─ data/
 │   ├─ local/           Room: Entity, DAO, Database, Converters, ค่าเริ่มต้นหมวดหมู่
 │   ├─ model/            data class ที่ใช้ข้าม layer (TransactionType, TransactionWithCategory)
 │   └─ repository/       ครอบ DAO ให้ ViewModel เรียกใช้งาน
 ├─ datastore/            การตั้งค่า (Theme/Currency/Sound/Vibration) ผ่าน DataStore
 ├─ ui/
 │   ├─ theme/            Color, Typography, Theme (Light/Dark)
 │   ├─ navigation/       NavGraph + Screen routes
 │   ├─ components/       Composable ใช้ซ้ำ (BottomBar, TransactionRow, DonutChart)
 │   └─ <feature>/        แต่ละหน้าจอมี Screen.kt + ViewModel.kt แยกกัน (MVVM)
 └─ util/                 Formatter, CSV backup/restore, ViewModel factory
```

- **MVVM**: แต่ละหน้าจอมี ViewModel เก็บ `StateFlow<UiState>` และ Screen เป็น Composable ที่ observe state
- **Room**: เก็บ Transaction/Category ในเครื่อง, ใช้ Flow เพื่อให้ UI อัปเดตอัตโนมัติเมื่อข้อมูลเปลี่ยน
- **ไม่ใช้ Dependency Injection framework** (ไม่มี Hilt) เพื่อลดความซับซ้อน — ใช้ `ExpenseTrackerApp` (Application class) เป็นที่เก็บ repository แบบ singleton และ `GenericViewModelFactory` แทน

## การ Build ในเครื่อง

ต้องมี Android Studio (Giraffe ขึ้นไป) หรือ Android SDK + JDK 17

```bash
./gradlew assembleDebug
```

APK จะอยู่ที่ `app/build/outputs/apk/debug/app-debug.apk`

> หมายเหตุ: โปรเจกต์นี้ไม่ได้ commit ไฟล์ `gradle-wrapper.jar` (ไฟล์ binary) มาด้วย
> ถ้า build ในเครื่องครั้งแรก ให้รันคำสั่งนี้ก่อนเพื่อสร้างไฟล์ wrapper (ต้องมี Gradle ติดตั้งในเครื่องก่อน):
> ```bash
> gradle wrapper --gradle-version 8.7
> ```

## การ Build ผ่าน GitHub Actions

Workflow อยู่ที่ `.github/workflows/build.yml` จะทำงานอัตโนมัติเมื่อ push ขึ้น branch `main`
หรือกดรันเองผ่านแท็บ Actions → Build APK → Run workflow

ผลลัพธ์ (debug + release APK ที่ยังไม่ได้ sign) จะถูกอัปโหลดเป็น **Artifacts** ให้ดาวน์โหลดได้ที่ท้าย
การรัน workflow แต่ละครั้ง

## การสำรอง/กู้คืนข้อมูล

ตั้งค่า → ข้อมูล → สำรองข้อมูล (Export CSV) / กู้คืนข้อมูล (Import CSV)
ไฟล์ CSV มีคอลัมน์: `id,type,amount,categoryId,dateEpochDay,timeMinutes,note,createdAtMillis`
การนำเข้าจะ**เพิ่ม**รายการใหม่เข้าไปในฐานข้อมูล (ไม่ลบข้อมูลเดิม) เพื่อป้องกันข้อมูลหาย
