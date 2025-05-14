### Step 1: Create a Database and User
```sql
CREATE DATABASE url_shortener;

CREATE USER 'shortener_user'@'%' IDENTIFIED BY 'shortener_user_123';

GRANT ALL PRIVILEGES ON url_shortener.* TO 'shortener_user'@'%';

FLUSH PRIVILEGES;
```