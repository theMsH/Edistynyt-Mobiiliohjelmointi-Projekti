# Edistyneen mobiiliohjelmoinnin projekti

* Sovellus vaatii kaikkia toimintoja varten rekisteröitymisen backendiin.
    * Token välitetään headereissa backendille. 
    * Kaikki eivät sitä kuitenkaan vaadi, joten autorisointi on toteutettu ohjelmallisesti sovelluksen koodissa.
* Autorisaatio on toteutettu tallentamalla token RoomDb.
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
   
Kaikkiin kirjautumista vaativiin API requesteihin on liitetty headerissa token, vaikkei backend sitä vaadi.

Lisäksi vaatimuksista poiketen sovelluksessa on huomioitu landscape orientation ja tumma teema.
