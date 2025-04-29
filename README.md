# Сервис поиска N-го минимального числа в XLSX-файле

Микросервис на Spring Boot для определения N-го минимального числа из первого столбца XLSX-файла.

## Требования
- **JDK 17+** (рекомендуется Temurin 17)
- **Apache Maven 3.8+**
- Любой современный браузер (для Swagger UI)

##  Быстрый старт

### 1. Клонируйте репозиторий

git clone https://github.com/your-repo/nth-min-service.git
cd nth-min-service
### 2. Соберите приложение
mvn clean install
### 3. Запустите сервис

java -jar target/nth-min-service.jar
Сервис будет доступен на http://localhost:8080.

# Использование
Через Swagger UI
Откройте в браузере: http://localhost:8080/swagger-ui.html

Найдите эндпоинт POST /api/numbers/find-nth-min

Заполните параметры:

file: Выберите XLSX-файл

n: Введите целое число > 0