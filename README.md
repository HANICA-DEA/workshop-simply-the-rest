# Introductie
Deze workshop is deel van de DEA Course aan de Hogeschool Arnhem/Nijmegen. 
Onderwerp is het bekend raken met JavaEE en JAX-RS in het bijzonder.

# Oefening
In deze workshop wordt stap-voor-stap een RESTful API gemaakt, met behulp van JAX-RS. We
zullen ingaan op het maken van REST Resources en het afhandelen van verschillende HTTP methodes. 
Ook zullen we werken met verschillende *Media Types* en zullen we automatisch [JSON](https://www.json.org/)
genereren en inlezen.

Tot slot zullen we ook nette foutafhandeling toe voegen, met behulp van 
[ExceptionMappers](https://dennis-xlc.gitbooks.io/restful-java-with-jax-rs-2-0-en/cn/part1/chapter7/exception_handling.html). 

## Voorbereiding
Voor deze oefening moet [TomEE 8.0.4 Plus](https://tomee.apache.org/download-archive.html) geïnstalleerd zijn en zodanig 
geconfigureerd dat het mogelijk is vanuit de IDE een JavaEE War kunt deployen. Als dit nog niet gelukt is, doorloop dan 
[deze](https://www.baeldung.com/tomcat-deploy-war) stappen tot en met stap 3.5.

## 1: Aanmaken nieuwe JavaEE 8 project
Gebruik Maven command line voor het aanmaken van een nieuw Java project. Gebruik hiervoor een archetype voor een standaard 
Java project. Zorg ervoor dat je Maven command line kan compileren _mét_ lambda expressies. Maak je project leeg (verwijder 
eventuele klassen die je van je archetype krijgt).

Om hier nu een JavaEE 8 project te maken moeten de volgende stappen worden genomen:

* Als default zal *Maven* de gecompileerde klassen samenbundelen tot een `.jar` bestand. Voor een JavaEE project moet dit
echter een `.war` bestand zijn. Lees op de website van [Maven](https://maven.apache.org/pom.html) na hoe je dit aanpast
* Voeg een dependency toe op [JavaEE](https://mvnrepository.com/artifact/javax/javaee-api)

Tot slot moeten we de klassen toevoegen die in de huidige repository te vinden zijn. Dit betreft de volledige `services`
map, inclusief alle submappen en bestanden.

* Kopieer de map `services` in de package structuur van je project. Zorg ervoor dat alle imports kloppen voor de 
klassen uit `services` en dat je je project kunt compileren

De klassen uit `services` zullen we verderop gaan gebruiken.

## 2: Aanmaken eerste REST-Resource
Om snel te kunnen testen of de applicatie wel wil deployen is het raadzaam als eerste een REST-Resource te maken
die daarvoor gebruik kan worden en die verder geen complexiteit toevoegt. 

* Maak een klasse `HealthCheckResource` met de volgende inhoud:

```java
@Path("/health")
public class HealthCheckResource {

    @GET
    public String healthy() {
        return "Up & Running";
    }
}
```
    
Ga voor dit project in IntelliJ naar Edit Configuration (naast de Run en Debug knoppen bovenin het scherm). Klik links 
op het plusje `+` en kies voor `TomEE Server -> Local`. Klik naast Application Server op Configure, klik op het plusje `+` 
en maak een nieuwe server-configuratie aan die verwijst naar de map waar je TomEE hebt geinstalleerd/uitgepakt en druk op 
OK. Onderin de dialoog staat een melding `Warning: No artifacts marked for deployment`, selecteer om dit probleem op te 
lossen in de Run-Configuratie van TomEE in IntelliJ onder het tabje Deployment de plus (`+`, Artifact) en selecteer de 
bovenste (eindigt op `:war`). Kies in deze dialoog de waarde `/` voor de Application Context. Op deze manier is de war-file 
die gebouwd wordt door IntelliJ/Maven gekoppeld aan de applicatieserver en start de server de juiste applicatie.
    
* Run nu je applicatie in TomEE en navigeer via de browser naar de url [http://localhost:8080/health](http://localhost:8080/health). 

Merk op dat een Rest-Resource een simpele, standaard Java-Klasse is. Ook wel een 
[POJO](https://en.wikipedia.org/wiki/Plain_old_Java_object) (Plain Old Java Object) genoemd. Boven de klasse
staat de annotatie die aangeeft via welk pad de Resource te benaderen is. De annotatie boven de methode
geeft aan welke HTTP-methode op welke Java methode gemapped wordt.
 
---

**Zorg dat dit werkt!**

Indien je **niet** de tekst `Up & Running` ziet in je browser, kan dit verschillende oorzaken hebben.
Het kunnen fouten zijn bij de configuratie van TomEE, fouten in je project of de code en fouten bij het
deployen. Begin dus eerst eens opnieuw met stap 1 en 2 en lees de stappen nog eens goed. Als dat ook niets op lost kun 
je een van onderstaande tips proberen:

* Als je een waarde hebt staan bij het invoerveld *Application Context*
op de *Deployment*-tab van de *Run-configuratie* in je IDE, zorg dan dat hier enkel de root `/` staat
  * Als je zeker weet dat je dit goed gezet hebt en deze instelling ook nergens wordt overschreven in je TomEE configuratie, 
dan kan het zijn dat je niet versie 8.0.4 van TomEE gebruikt. Versie 8.0.6 heeft hier namelijk een probleem mee
* Het kan ook zijn dat je fouten krijgt over een missend web.xml bestand (een zgn. deployment descriptor). Voeg voor nu dan 
de volgende property toe aan de `<properties>` van je pom.xml: ```<failOnMissingWebXml>false</failOnMissingWebXml>```

Als je bovenstaande tips hebt toegepast en je ziet (nog steeds) foutmeldingen die wijzen op iets niet kunnen deployen, dan is er vermoedelijk sprake van een rechtenprobleem: herstart IntelliJ als Administrator. 

---

## 3: Een REST-Resource voor het ophalen van Items
Voeg een nieuwe REST-Resource toe voor het ophalen van Items. Deze moet aan de volgende eigenschappen voldoen:
* De Resource is te bereiken via het pad `/items`
* De Resource wordt aangesproken middels een `GET` request.
* De volgende String wordt geretourneerd: `"bread, butter"`
* De Resource produceert `text/plain`

Deploy je applicatie op TomEE en test of deze nieuwe Resource via de browser te bereiken is.

## 4: Het retourneren van de Items als [JSON](https://www.json.org/)
Voeg een nieuwe methode toe die het ook mogelijk maakt de twee items als een lijst te retourneren. Voor nu doen we dit
nog steeds als een String, maar dan in het [JSON](https://www.json.org/) formaat. Later zullen we dit dan eenvoudiger
kunnen omzetten naar een daadwerkelijke lijst van items. De Resource moet aan de volgende eigenschappen voldoen:
* De Resource is te bereiken via hetzelfde pad `/items`
* De Resource wordt aangesproken middels een `GET` request.
* De volgende String wordt geretourneerd: `"["bread", "butter"]"`
* De Resource produceert `application/json`

Merk op dat je nu dus hardcoded [JSON](https://www.json.org/) retourneert. De dubbele quotes zul je moeten 
[escapen](https://stackoverflow.com/questions/3844595/how-can-i-make-java-print-quotes-like-hello/42660163). Dit doe 
je op de volgende manier:

```
"[\"bread\", \"butter\"]";
``` 
Deploy je applicatie op TomEE en test of deze nieuwe Resource via de browser te bereiken is. Welke representatie
ontvang je nu terug? De `text/plain` of de `application/json`?

## 5: Gebruik Postman voor het testen van je applicatie
Via de browser is het lastig om nu te kunnen kiezen tussen de twee representaties van de *Items*. Om dit
te doen moeten we de HTTP-Header `Accept` toevoegen en daar de gewenste representatie invullen.

Een veel gebruikte tool hiervoor is [Postman](https://www.getpostman.com/downloads/). Dit is een applicatie waarmee je
eenvoudig client requests kan versturen en analyseren. Download en installeer deze en gebruik hem voor het handmatig 
verzenden van het request en het zetten van de `Accept` HTTP-Header. Test of het lukt om beide representaties op te vragen. 

Test vervolgens of je dit ook met één enkele Resource kunt doen. Pas daarvoor de Resource zo aan dat je meerdere 
*Media-Types* accepteert. Onderzoek ook de volgorde effecten van de `@Produces` annotatie:
* Gebruik `Accept = */*`. Gebruik `@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })` en varieer de volgorde
* Gebruik `Accept = */*` en `@Produces({ "application/*", MediaType.TEXT_PLAIN })`, wat valt op?

Wij gaan voor de rest van de workshop alleen [JSON](https://www.json.org/) consumeren (i.e. requests) en produceren
(i.e. responses), dus zorg dat je hiervoor een juiste `/items` Resource hebt.

## 6: Automatisch [JSON](https://www.json.org/) genereren van Java-Objecten
Om van onze String return-waarde nu een echte lijst te kunnen maken kunnen we JavaEE functionaliteit gebruiken. Het is met 
JavaEE namelijk mogelijk om automatisch JSON te genereren van Java-Objecten. Wanneer je methode een Java-Object retourneert, 
dan zal JavaEE (specifiek: de JSON-B library van JAX-RS) automatisch proberen het Object te converteren naar het *Media Type*
dat je via `@Produces(MediaType.APPLICATION_JSON)` hebt aangegeven. 

Spelregels hiervoor zijn dat de betreffende class moet voldoen aan de zgn. "[bean specificatie](https://docstore.mik.ua/orelly/java-ent/jnut/ch06_02.htm)":
* De class heeft een parameterloze constructor
* Alle properties zijn private
* Alle properties hebben een getter en setter (zie 6.2.2 van bovenstaande bean specificatie)

Pas de voorgaande methode aan die [JSON](https://www.json.org/) retourneert:

* Het return type is niet langer een String, maar een `List<ItemDTO>`. Gebruik hiervoor ook
de Klasse `ItemService` die een methode heeft om een  `List<ItemDTO>` te retourneren
* Declareer in je Resource-klasse een variabele van het type `ItemService` en gebruik de constructor om deze te instantiëren

Test je Resource met Postman en bekijk hoe de JSON in je response er nu uit ziet. Kan je de structuur verklaren als je kijkt 
naar het Java object?

## 7: Retourneren van een `Response` object
In de vorige opdracht heb je het return-type van de methode aangepast naar een `List<ItemDTO`. De inhoud van deze lijst 
wordt door JSON-B automatisch omgezet naar [JSON](https://www.json.org/) en vervolgens aan de *body* van het HTTP-response 
toegevoegd. De inhoud van de *headers* en de statuscode wordt automatisch ingevuld. Wil je hier meer invloed op hebben, 
dan kun je ook een `Response` object retourneren van de Jersey library van JAX-RS. Bij een dergelijk Object kun je ook 
zelf aangeven welke Http-headers en -statuscode je wilt retourneren. Pas je Resource op de volgende manier aan:
* Het return type is niet langer een `List<ItemDTO>`, maar een `Response`. Let hierbij op dat je de juiste `Response` 
importeert. Het betreft deze: `javax.ws.rs.core.Response`.
* Creëer een `Response` met `List<ItemDTO>` als *entity* en een statuscode 200. Gebruik deze Resource
om te achterhalen hoe je een dergelijk Object moet maken: [Set a Response Body in JAX-RS](https://www.baeldung.com/jax-rs-response)

Controleer dat het retourneren van verschillende statuscodes naar je client werkt.

## 8: Creëer een REST-Resource voor het ophalen van een enkel *item*
Voeg aan je klasse een nieuwe REST-Resource toe die het mogelijk maakt om een enkel *item* op basis van het *id* te retourneren.
Pas je Resource wederom aan:
* De Resource is te bereiken via het pad `/items/{id}`
* De Resource wordt aangesproken middels een GET request
* Het volledige *item* wordt geretourneerd
* De Resource produceert `application/json`
* Implementeer enkel de happy-flow

De vraag is nu vooral hoe om te gaan met de `id` uit het pad. Raadpleeg daarvoor deze bron: 
[JAX-RS path params](https://www.mkyong.com/webservices/jax-rs/jax-rs-pathparam-example/). Om erachter te komen welke id's
geldig zijn kan je alle items ophalen en deze response bekijken, om te voorkomen dat je een `ItemNotAvailableException` 
krijgt op je nieuwe Resource.

## 9: Creëer een REST-Resource voor het toevoegen van een *item*
Voeg aan je klasse een nieuwe REST-Resource toe die het mogelijk maakt om een *item* via de `ItemService` toe 
te voegen.

* De Resource is te bereiken via het pad `/items`
* De Resource wordt aangesproken middels een `POST` request
* De methode zal nu een `ItemDTO` als parameter hebben. Wanneer je een correcte JSON in de body van je request meestuurt, 
zal JAX-RS dit automatisch omzetten naar een correct Java-Object
* De Resource consumeert `application/json`
* Er wordt een HTTP-Status *Created* geretourneerd. Zoek op welke code dat is
* Implementeer enkel de happy-flow
* Gebruik Postman voor het testen van je nieuwe Resource. Bekijk goed hoe je JSON eruit moet zien

Na het toevoegen van een POST kun je via een GET naar `/items` bekijken of het nieuwe item is toegevoegd. De kans is groot
dat dat niet zo zal zijn. Dit komt omdat TomEE voor **ieder** request een nieuwe instantie van de `ItemResource` klasse 
maakt. En daarmee ook van de klasse `ItemService`, waarmee de lijst van items weer op zijn default waarde wordt 
geïnitialiseerd.

In een normale applicatie zul je dan ook een database gebruiken voor het bijhouden en managen (= persisteren) van je data. 
In dit geval doen we dat niet en kun je het probleem oplossen door `@Singleton` boven je `ItemResource`-klasse te zetten. 
Gebruik de `javax.ejb.Singleton` klasse. Daarmee maakt JAX-RS maar één instantie aan, die over verschillende requests wordt 
hergebruikt. Merk op dat je nu wél je nieuwe `Item` terug krijgt als je deze aan maakt en vervolgens ophaalt.

## 10: Toevoegen foutafhandeling
In voorgaande twee onderdelen heb je enkel de happy-flow geïmplementeerd. Er zijn echter twee situaties waar het mis kan 
gaan. Kijk maar naar de code van `ItemService`. In beide gevallen wordt een *unchecked* `Exception` gegooid, welke 
uiteindelijk bij je REST-Resource van JAX-RS uitkomt. JAX-RS handelt dit verder af en stuurt een standaard HTTP-Response
terug met een foutcode. Het resultaat is een `Internal Server Error` (statuscode `500`). Deze statuscode is bedoeld voor 
kritieke applicatie fouten. In deze opdracht gaan we dit netter oplossen, omdat we dat kunnen. 

Zorg ervoor dat je zelf een specifieke foutcode teruggeeft in beide situaties:
* In het geval van een `ItemNotAvailableException` behoor je een HTTP-Statuscode 404 terug te geven
* In het geval van een `IdAlreadyInUseException` behoor je een HTTP-Statuscode 409 terug te geven

Misschien is je eerste gedachte dit met een `try...catch` op te lossen. In de `try` roep je de `ItemService` aan en in de 
`catch` retourneer je een `Response`-object met de juiste status-code. Dat zal zeker werken, maar heeft tot gevolg dat je 
overal `try...catch` constructies in je code krijgt. Het kan aanzienlijk eleganter.

Gebruik deze informatie voor het correct implementeren van de foutafhandeling: [Exception Handling](https://dennis-xlc.gitbooks.io/restful-java-with-jax-rs-2-0-2rd-edition/content/en/part1/chapter7/exception_handling.html)

## 11: Implementeer ook het verwijderen van een *item*
Implementeer tot slot ook het verwijderen van een *item*. De `ItemService` bevat hier al de benodigde methode voor.
* Bedenk zelf via welk pad de Resource te bereiken is
* Bedenk welke HTTP-Methode er gebruikt moet worden
* Vergeet ook de foutafhandeling niet te implementeren (of is dat al gedaan??)
