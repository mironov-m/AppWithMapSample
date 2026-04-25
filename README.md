# Project Overview


https://github.com/user-attachments/assets/dc44b0e4-a3ff-4a35-b1df-0f7f07af8cd4


## Key Technologies

- **Target Platforms**: Android
- **Primary Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVI (Model-View-Intent) with Orbit MVI
- **Navigation**: Compose Navigation
- **Dependency Injection**: Hilt
- **Network**: Ktor

## Architecture

### Gradle-layer

На данном этапе это одномодульное приложение-монолит. 
Но фичи должны разрабатываться с соблюдением слоев, чтобы при необходимости,
было легко их разнести по модулям.

### App-layer

Верхнеуровневый взгляд на приложение в разрезе разбивки на модули (пакеты):

app/
└── app/
    ├── MainActivity.kt
    ├── App.kt
    │
    ├── navigation/
    │   ├── NavGraph.kt
    │   └── Destinations.kt
    │
    └── di/
        ├── AppModule.kt
        └── ...

└── core/
    ├── network/
    ├── ui/
    ├── navigation/
    └── common/

└── feature/
    ├── list/
    ├── detail/
    └── ...

└── domain/
    ├── cities/
    └── ...

└── data/
    ├── cities/
    └── ...

core/* - Общий код, переиспользуемый между фичами:

network — Ktor клиент, конфигурация
ui — общие composables, тема
navigation — навигационные контракты

### Feature-layer

Фича - независимый функциональный блок, основные принципы:
- фича не зависит от другой фичи напрямую (взаимодействие только через контракты)
- presentation слой зависит от domain, domain от data
- domain и data могут переиспользоваться несколькими фичами
- в качестве упрощения структуры данных из data слоя могут быть в любом слое (приемлем эту протечку для упрощения разработки на данном этапе)

Пример структуры:

feature/list/
└── presentation/
│    ├── ListViewModel.kt
│    ├── ListState.kt
│    └── ListSideEffect.kt
│
└── ui/
│    └── ListScreen.kt
│
└── di/
    └── ListModule.kt

Repository обычно возвращает flow<Resource<T>>, где [Resource](ru.mironov.appwithmapsample.core.utils.resource.Resource) - это обертка
```kotlin
sealed interface Resource<T : Any> {

    data class Success<T : Any>(val value: T) : Resource<T>

    class Loading<T : Any> : Resource<T>

    data class Error<T : Any>(val throwable: Throwable) : Resource<T>
}
```
