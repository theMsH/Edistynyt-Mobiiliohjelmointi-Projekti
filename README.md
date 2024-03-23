# Edistyneen mobiiliohjelmoinnin projekti

* Sovellus vaatii kaikkia toimintoja varten rekisteröitymisen backendiin.
    * Token välitetään headereissa backendille. 
    * Kaikki API requestit eivät sitä kuitenkaan vaadi, joten autorisointi on toteutettu ohjelmallisesti sovelluksen koodissa.
* Autorisaatio on toteutettu tallentamalla token RoomDb sekä AuthInterceptoriin.
    * Sovelluksessa on automaattinen sisäänkirjautuminen

## Sovelluksen vaatimukset
    - Jetback Compose MVVM arkkitehtonisen mallin mukaisesti
    - Sisäänkirjautuminen*
    - Uloskirjautuminen*
    - Uuden käyttäjän rekisteröiminen
    - Kategorioiden listaus LazyColumniin
    - Kategorioiden nimen muokkaus*
    - Kategorian lisääminen*
    - Kategorian poisto*
    - Tavaroiden listaus LazyColumniin
    - Tavaroiden nimen muokkaus*
    - Tavaroiden lisäämienn*
    - Tavaroiden poistaminen*
    - Tavaroiden vuokraus*
    - Lokalisointi
    - RoomDB: Tokenin tallennus, poisto, kysely

    *Sovelluksen loginvaatimuksia voi testata kirjautumatta sisään.

## Extra: Sovelluksen ei vaadittuja ominaisuuksia
    - Landscape asento on huomioitu
    - Tumma teema on huomioitu
    - Näppäimistöissä helppokäyttöisyystoimintoja
        - Luonti dialogissa on näppäimistön autofocus
        - Näppäimistön KeyboardType muuttuu TextFieldin käyttötarkoituksen mukaisesti
        - Näppäimistön ImeAction muuttuu käyttötarkoitusten mukaisesti
    - Salasanan voi piilottaa/nähdä
    - Kategorian poiston yhteydessä tarkistetaan kategorian sisältö
        - Poistoa ei yritetä suorittaa, mikäli kategoriassa on tavaroita
    - Kategorian nimi tarkistetaan päällekkäisnimeämisen varalta luonnin sekä nimen muokkauksen yhteydessä 
        - Käyttäjälle näytetään virheviesti, jos nimi on jo käytössä
