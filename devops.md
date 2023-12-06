# DevOps v aplikácii Petals

Aplikácia petals podporuje CI/CD (continuous integration/continuous deployment) pomocou Github Actions.  
Jednotlivé súbory sa nachádzajú v adresári Petals/.github/workflows, tieto sú:
- build.yaml
- dependency-license-analysis.yaml
- lint.yaml
- unit-tests.yaml
- release.yaml
## build.yaml
**Názov**: Build Universal APK
**Triggers**: push, pull_request - automaticky sa vykoná pri každom pushnutí commitov do repozitára a zadania pull requestu
**Jobs**: obsahuje 2 joby:
1. **check_yaml_consistency** - príprava yaml súboru, 3 kroky:
    -  checkout aktuálneho repozitára
    -  spustenie scriptu, ktorý odstráni aktuálnu verziu súboru **build.yaml** znovu ho vygeneruje
    - otestovať konzistenciu súboru **build.yaml**, či je správne vygenerovaný
2. **build** - 3 kroky:
    - príprava prostredia JDK s verzou 17
    - checkout aktuálneho repozitára
    - spustenie gradle-build na vykonanie a buildu a získanie .apk súboru
## dependency-licence-analysis.yaml
**Názov**: Dependency License Analysis
**Triggers**: push, pull_request - automaticky sa vykoná pri každom pushnutí commitov do repozitára a zadania pull requestu
**Jobs**: obsahuje 2 joby:
1. **check_yaml_consistency** -príprava yaml uboru, to isté, ako pri **build.yaml**, len pre **dependency-licence-analysis.yaml**
2. **build** -  4 kroky:
    -   príprava prostredia JDK s verzou 17
    -   checkout aktuálneho repozitára
    - spustenie gradle-build na analýziu licencií jednotlivých závislostí a vytváranie súboru **licenses.md**
    - pripájanie súboru **licenses.md** k súhrnu krokov Github Actions
## lint.yaml
**Názov**: Lint
**Triggers**: push, pull_request - automaticky sa vykoná pri každom pushnutí commitov do repozitára a zadania pull requestu
**Jobs**: obsahuje 2 joby:
1. **check_yaml_consistency** - príprava yaml uboru, to isté, ako pri **build.yaml**, len pre **lint.yaml**
2. **detekt**- 3 kroky
    -   príprava prostredia JDK s verzou 17
    -   checkout aktuálneho repozitára
    - spustenie gradle-build na statickú analýzu kódu pomocou Lint, ktorý pomôže pri zvyšovaní kvality kódu, zachytaní chýb a zabezpečení celkovej konzistencie kódu
## unit-tests.yaml
**Názov**: Unit Tests
**Triggers**: push, pull_request - automaticky sa vykoná pri každom pushnutí commitov do repozitára a zadania pull requestu
**Jobs**: obsahuje 2 joby:
1. **check_yaml_consistency** - príprava yaml uboru, to isté, ako pri **build.yaml**, len pre **unit-tests.yaml**
2. **unit-tests** - 3 kroky
    -   príprava prostredia JDK s verzou 17
    -   checkout aktuálneho repozitára
    -  spustenie gradle-build na vykonanie gradle test, ktorý spúšťa všetky unit testy v projekte
## release.yaml
**Názov**: Release
**Triggers**: push, '*' - automaticky sa vykoná pri každom pushnutí commitov do repozitára s podmienkou, keď push obsahuje daný tag. Tento tag znamená, že sa spustí pri akomkoľvek tagu
**Jobs**: obsahuje 2 joby:
1. **check_yaml_consistency** -príprava yaml uboru, to isté, ako pri **build.yaml**, len pre **release.yaml**
2. **create-apk** -  7 krokov:
    -   príprava prostredia JDK s verzou 17
    -   checkout aktuálneho repozitára
    - odhalenie GPG kľúča, ktorý slúži na podpis releasu
    -  spustenie gradle-build na vykonanie a buildu a získanie .apk súboru pre release
    -  vytvorenie nového releasu
    - pomocou setup-ruby nastavenie verziu Ruby na 2.6
    - pomocou fastlane publikovanie nového releasu na Play Store