# 💰 Expense Tracker  

An **Expense Tracker Web Application** built with **Spring Boot**, **Thymeleaf**, and **Gradle**, designed to help users manage their personal finances efficiently.  
The system supports **user authentication, expense categorization, reminders, budget alerts, admin dashboard, and AI-powered insights**.

---

## 🚀 Features  
- 🔐 **User Authentication** – Secure login/signup with Spring Security  
- 📊 **Expense Management** – Add, update, delete, and view expenses  
- ⚠️ **Budget Alerts** – Notifications when spending exceeds limits  
- ⏰ **Reminders** – Get reminders for bills and due payments  
- 🛠 **Admin Dashboard** – Manage users and monitor activities  
- 🤖 **AI-Powered Chat (LLM Service)** – Provides insights or assistance related to expenses  
- 📧 **Email Service** – Sends notifications and reminders via email  

---

## 🛠 Tech Stack  
- **Backend**: Spring Boot (Java)  
- **Frontend**: Thymeleaf, HTML, CSS, JS
- **Security**: Spring Security (Authentication & Authorization)  
- **Database**: MySQL *(configurable in `application.properties`)*  
- **Build Tool**: Gradle  
- **IDE**: Eclipse / IntelliJ IDEA  

---


---

## ⚙️ Setup Instructions  

### 1️⃣ Clone Repository  
```bash
git clone https://github.com/yourusername/ExpenseTracker.git
cd ExpenseTracker

./gradlew build

The app will start on:
👉 http://localhost:6788

4️⃣ Access Endpoints
Login: /auth/login
Signup: /auth/signup
User Dashboard: /dashboard
Admin Dashboard: /dashboard/admin
