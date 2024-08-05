## Описание проекта:
Простое SpringBoot приложение с использование Spring OAP, для логирования операций в приложении.
Данное приложение имитирует управление пользователями и их заказами. Используемый стек технологий: 
- SpringBoot
- Spring Data Jpa
- Spring Web
- Spring OAP
- PostgreSQl
- Swagger

## Информация по endpoint'ам доступна здесь:
http://localhost:3000/swagger-ui/index.html#/

### Список аспектов

* ValidAspect - Проверяет параметры/поля классов, которые помечены аннотациями @NotNull, @NotBlank, @Regex (Параметр или класс с полями должны иметь аннотацию @Valid) Приоритет: 1
* LoggingAspect - выдает информацию о выполнение методов в сервисах (в классах, которые помечены аннотацией @Service) Приоритет: 2. Занимается логированиемследующих вещей:
* 1. Информация о начале выполнения метода с переданными параметрами
  2. Информация об успешном завершении метода с его возвращаемым значением  
  3. Информация об успешном завершении метода с void сигнатурой 
  4. Информация о возникновении исключения в методе

### Логирование

- создан и настроен log4j2 файл
- все логи пишутся в консоль, а также в файл, который сохраняется в папке logs
- 
## Инструкция по запуску