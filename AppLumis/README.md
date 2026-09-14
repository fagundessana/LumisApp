# AppLumis — Home Lumis (Kotlin + Jetpack Compose + Material3)

Reprodução fiel da referência de alta fidelidade da **Home do aplicativo Lumis**.

## Onde cada arquivo vai (já criados nesta estrutura)

```
settings.gradle.kts
build.gradle.kts
gradle.properties
gradle/wrapper/gradle-wrapper.properties
app/build.gradle.kts
app/src/main/AndroidManifest.xml
app/src/main/java/com/example/lumis/MainActivity.kt              // NavHost + Scaffold
app/src/main/java/com/example/lumis/navigation/LumisNav.kt       // rotas (adicione futuras aqui)
app/src/main/java/com/example/lumis/ui/theme/Color.kt            // TODAS as cores
app/src/main/java/com/example/lumis/ui/theme/Type.kt             // Montserrat / Merriweather / Questrial
app/src/main/java/com/example/lumis/ui/theme/Theme.kt            // LumisTheme claro
app/src/main/java/com/example/lumis/ui/home/HomeScreen.kt        // tela principal
app/src/main/java/com/example/lumis/ui/home/model/HomeUiModel.kt
app/src/main/java/com/example/lumis/ui/home/components/TopLocationBar.kt
app/src/main/java/com/example/lumis/ui/home/components/PromoBanner.kt
app/src/main/java/com/example/lumis/ui/home/components/SectionHeader.kt
app/src/main/java/com/example/lumis/ui/home/components/CategorySection.kt
app/src/main/java/com/example/lumis/ui/home/components/PopularServicesGrid.kt
app/src/main/java/com/example/lumis/ui/home/components/RecommendedSection.kt
app/src/main/java/com/example/lumis/ui/home/components/LumisBottomBar.kt
app/src/main/java/com/example/lumis/ui/placeholder/PlaceholderScreens.kt
app/src/main/res/values/strings.xml, colors.xml, themes.xml
app/src/main/res/drawable/placeholder_hero.xml, placeholder_clean_1/2.xml
app/src/main/res/font/LEIA-ME-FONTES.txt
```

## Como rodar no Android Studio

1. Abra o Android Studio → **Open** → selecione a pasta `AppLumis` (a que contém `settings.gradle.kts`).
2. Deixe o Studio sincronizar o Gradle (baixa AGP 8.7.3 + Kotlin 2.0.21 + Compose BOM 2024.12.01).
3. Se pedir SDK, aponte para `C:\Users\Pichau\AppData\Local\Android\Sdk` (compileSdk 36 já instalado).
4. Selecione um emulador ou dispositivo físico e clique em **Run ▶** (`MainActivity`).
5. Para ver sem emulador: abra `HomeScreen.kt` → aba **Split/Design** → Preview 360x800.

## Decisões pedidas por você

- **Placeholders/Coil:** `PromoBanner` e `ProfessionalCard` usam `coil.compose.AsyncImage`
  com `model = imageUrl ?: R.drawable.placeholder_*`. Troque depois por URL ou foto real
  sem mudar o componente. `INTERNET` já está no Manifest. Dependência: `io.coil-kt:coil-compose:2.6.0`.
- **Tipografia mobile:** headers de seção em 17-18sp ExtraBold (fidelidade + leitura em 360dp);
  24sp/32sp reservados para display/hero conforme spec original. Veja `Type.kt`.
  Para ativar as fontes reais, siga `res/font/LEIA-ME-FONTES.txt` e descomente o bloco em `Type.kt`.
- **Nav clicável e extensível:** `LumisRoute` (sealed) + `NavHost` em `MainActivity` +
  `LumisBottomBar` com `launchSingleTop + restoreState`. Nova página = nova rota + 1 `composable()`
  + (opcional) item em `bottomItems`. Telas Cidade/Explorar/Agenda/Perfil são placeholders.

## Cores (sem hex espalhado)

Tudo em `ui/theme/Color.kt` (`LumisColors`): Background `#F9F9FB`, SecondaryText `#666666`,
Primary `#50BF87`, Secondary `#3DBEA9`, Tertiary `#508ABF`, hovers `#317252/#2E8C7E/#3168A2`,
OnButton `#F9F9FB/#D3D3D3`, LightGray `#F5F5F5` + derivados de fidelidade
(BadgeYellow, CardTint, ExplorerCircle, StarYellow, Border, Scrim).
