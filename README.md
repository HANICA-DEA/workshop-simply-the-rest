# Introductie
Deze workshop is deel van de DEA Course aan de Hogeschool Arnhem/Nijmegen. 
Onderwerp is het bekend raken met JavaEE en JAX-RS in het bijzonder.

# Oefening
In deze workshop wordt stap-voor-stap een Restfull API gemaakt, met behulp van JAX-RS. We
zullen ingaan op het maken van REST resources en het afhandelen van verschillende HTTP methodes. 
Ook zullen we werken met verschillende mediatypes en zullen we automatisch [JSON](https://www.json.org/)
genereren en inlezen.

Tot slot zullen we ook nette foutafhandeling toe voegen, met behulp van 
[ExceptionMappers](https://dennis-xlc.gitbooks.io/restful-java-with-jax-rs-2-0-en/cn/part1/chapter7/exception_handling.html). 

## Voorbereiding
Voor deze oefening moet [TomEE Plus](tomee.apache.org/download-ng.html) geïnstalleerd zijn en zodanig 
geconfigureerd dat het mogelijk is vanuit de IDE een JavaEE War kunt deployen. 

## 1: Aanmaken nieuwe JavaEE 8 project
Gebruik Maven voor het aanmaken van een nieuw JavaEE 8 project. Gebruik hiervoor een archetype voor een standaard
Java project. 

Om hier nu een JavaEE 8 project te maken moeten de volgende stappen worden genomen:

* Als default zal *Maven* de gecompileerde klassen samenbundelen tot een `.jar` bestand. Voor een JavaEE project moet dit
echter een `.war` bestand zijn. Lees op de website van [Maven](https://maven.apache.org/pom.html) na hoe je dit aanpast.
* Voeg een dependency toe op [JavaEE](https://mvnrepository.com/artifact/javax/javaee-api)

Tot slot moeten we de klassen toevoegen die in deze repository te vinden zijn. Dit betreft de volledige `services`
map, inclusief alle submappen en bestanden.

* Kopieëer de map `services` in de package structuur van je project. Zorg ervoor dat alle imports kloppen voor de 
klassen uit `services` en dat je je project kunt compileren.

De klassen uit `services` zullen we verderop gaan gebruiken.

Ga voor dit project in IntelliJ naar Edit Configuration (naast de Run en Debug knoppen bovenin het scherm). Klik links op het plusje (+) en kies voor TomEE Server -> Local. Klik naast Application Server op Configure, klik op het plusje (+) en maak een nieuwe server-configuratie aan die verwijst naar de map waar je TomEE hebt geinstalleerd/uitgepakt en druk op OK. Onderin de dialoog staat een melding "Warning: No artifacts marked for deployment", selecteer op dit probleem op te lossen de Fix-knop en kies de bovenste van de twee opties, het war-bestand. Op deze manier is de war die gebouwd wordt door IntelliJ/Maven gekoppeld aan de applicatieserver en start de server de juiste applicatie. 

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
* Deploy je applicatie op TomEE en navigeer via de browser naar de url [http://localhost:8080/health](http://localhost:8080/health). 

Merk op dat een Rest-Resource een simpele, standaard Java-Klasse is. Ook wel een 
[POJO](https://en.wikipedia.org/wiki/Plain_old_Java_object) (Plain Old Java Object) genoemd. Boven de klasse
staat de annotatie die aangeeft via welk pad de resource te benaderen is. De annotatie boven de methode
geeft aan welke HTTP-methode op welke Java methode gemapped wordt.
 
---

**Zorg dat dit werkt!**

Indien je **niet** de tekst *Up & Running* ziet, kan dit verschillende oorzaken hebben.
Het kunnen fouten zijn bij de configuratie van TomEE, fouten in je project of de code en fouten bij het
deployen. Tot slot is het ook denkbaar dat je een waarde hebt staan bij het invoerveld *Application Context*
op de *Deployement*-tab van de *Run-configuratie* in je IDE. Zorg dat hier enkel de root (*/*) staat.

Als je bovenstaande tips hebt toegepast en je ziet (nog steeds) foutmeldingen die wijzen op iets niet kunnen deployen, dan is er vermoedelijk sprake van een rechtenprobleem: herstart IntelliJ als Administrator. 

---

## 3: Een REST-Resource voor het ophalen van Items
Voeg een nieuwe REST-Resource toe voor het ophalen van Items. Deze moet aan de volgende eigenschappen 
voldoen:
* De Resource is te bereiken via het pad `/items`
* De Resource wordt aangesproken middels een GET request.
* De volgende String wordt geretourneerd: `"bread, butter"`
* De Resource produceert `text/plain`

Deploy je applicatie op TomEE en test of deze nieuwe Resource via de browser te bereiken is.

## 4: Het retourneren van de Items als [JSON](https://www.json.org/)
Voeg een nieuwe methode toe die het ook mogelijk maakt de lijst als [JSON](https://www.json.org/)
te retourneren. Deze moet aan de volgende eigenschappen voldoen:
* De Resource is te bereiken via het pad `/items`
* De Resource wordt aangesproken middels een GET request.
* De volgende String wordt geretourneerd: `"["bread", "butter"]"`
* De Resource produceert `application/json`

Merk op dat je nu dus hardcoded [JSON](https://www.json.org/) retourneert. De dubbele quotes zul je moeten 
[escapen](https://stackoverflow.com/questions/3844595/how-can-i-make-java-print-quotes-like-hello/42660163). Dit doe 
je op volgende manier:

```
"[\"bread\", \"butter\"]";
``` 
Deploy je applicatie op TomEE en test of deze nieuwe Resource via de browser te bereiken is. Welke representatie
ontvang je nu terug? De `text/plain` of de `application/json`?

## 5: Gebruik Postman voor het testen van je applicatie
Via de browser is het lastig om nu te kunnen kiezen tussen de twee representaties van de *Items*. Om dit
te doen moeten we de HTTP-Header ACCEPT toevoegen en daar de gewenste representatie invullen.

Een veel gebruikte tool hiervoor is [Postman](https://www.getpostman.com/downloads/). Download en 
installeer deze en gebruik hem voor het handmatig verzenden van het request en het zetten
van de ACCEPT HTTP-Header.

Test of het lukt om beide representaties op te vragen.

## 6: Automatisch [JSON](https://www.json.org/) genereren van Java-Objecten
Het is met JavaEE mogelijk om automatisch [JSON](https://www.json.org/) te genereren van Java-Objecten.
Wanneer je methode een Java-Object retourneert, dan zal JavaEE (meer specifiek: JAX-RS) automatisch proberen
het Object te converteren naar het *Media Type* dat je via `@Produces(MediaType.APPLICATION_JSON)` hebt
aangegeven. 

Spelregels hiervoor zijn dat de betreffende class moet voldoen aan de zgn. "[bean specificatie](https://docstore.mik.ua/orelly/java-ent/jnut/ch06_02.htm)":
* De class heeft een parameterloze constructor
* Alle properties zijn private
* Alle properties hebben een getter en setter (zie 6.2.2 docstore.mik.ua/orelly/java-ent/jnut/ch06_02.htm voor de precieze eisen)

Pas de voorgaande methode aan die [JSON](https://www.json.org/) retourneert:

* Het return type is niet langer een String, maar een `List<ItemDTO>`. Gebruik hiervoor ook
de Klasse `ItemService` die een methode heeft om een  `List<ItemDTO>` te retourneren.
* Voeg aan je Resource-klasse een instantie-variabele van het type `ItemService` toe en gebruik 
de constructor om hieraan een instantie toe te voegen:
```
@Path("/items)
public class ItemResource {
    private ItemService itemService;

    public ItemResource() {
        this.itemService = new ItemService();
    }

    ...

```

## 7: Retourneren van een `Response`
In de vorige opdracht heb je het return-type van de methode aangepast naar een `List<ItemDTO`. 
De inhoud van deze lijst wordt door JAX-RS automatisch omgezet naar [JSON](https://www.json.org/)
en vervolgens aan de *body* van het HTTP-response toegevoegd. De inhoud van de *header* wordt 
automatisch ingevuld. Wil je hier meer invloed op hebben, dan kun je ook een `Response` retourneren.
Bij een dergelijk Object kun je ook zelf aangeven welke HTTP-status code het de *header* moet bevatten.

* Het return type is niet langer een `List<ItemDTO>`, maar een `Response`. Let hierbij dat je IDE
de juiste `Response` importeert. Het betreft deze: `javax.ws.rs.core.Response`.
* Creeer een `Response` met `List<ItemDTO>` als *entity* en een statuscode 200. Gebruik deze resource
om te achterhalen hoe je een dergelijk Object moet maken: [Set a Response Body in JAX-RS](https://www.baeldung.com/jax-rs-response)

## 8: Creëer een REST-Resource voor het ophalen van een enkel *item*
Voeg aan je klasse een nieuwe REST-Resource toe die het mogelijk maakt om een enkele *item*, op basis
van het *id* te retourneren.

* De Resource is te bereiken via het pad `/items/:id`
* De Resource wordt aangesproken middels een GET request
* Het volledige *item* wordt geretourneerd
* De Resource produceert `application/json`
* Implementeer enkel de happy-flow

De vraag is nu vooral hoe om te gaan met de `:id` uit het pad. Raadpleeg daarvoor deze bron: 
[JAX-RS path params](https://www.mkyong.com/webservices/jax-rs/jax-rs-pathparam-example/)

## 9: Creëer een REST-Resource voor het toevoegen van aan *item*
Voeg aan je klasse een nieuwe REST-Resource toe die het mogelijk maakt om een *item* via de `ItemService` toe 
te voegen.

* De Resource is te bereiken via het pad `/items`
* De Resource wordt aangesproken middels een POST request
* De methode zal nu een `ItemDTO` als parameter hebben. Wanneer je een correcte 
[JSON](https://www.json.org/) in de body van je request meestuurt, zal JAX-RS dit automatisch
omzetten naar een correct Java-Object.
* De Resource consumeert `application/json`
* Er wordt een HTTP-Status *Created* geretourneerd. Zoek op welke code dat is
* Implementeer enkel de happy-flow
* Gebruik Postman voor het testen van je Resource. Bekijk goed hoe je 
[JSON](https://www.json.org/) eruit moet zien.

Na het toevoegen van een POST kun je via een GET naar `/items` bekijken of het nieuwe
item is toegevoegd. De kans is groot dat dat niet zo zal zijn. Dit komt omdat TomEE voor **ieder**
request een nieuwe instantie van de `ItemResource` klasse maakt. En daarmee ook van de klasse
`ItemService`, waarmee de lijst van items weer op zijn default waarde wordt geïnitialiseerd.

In een normale applicatie zul je dan ook een Database gebruik voor het persisteren van je Data. In 
dit geval doen we dat niet en kun je het probleem oplossing door `@Singleton` boven je `ItemResource`-klasse
te zetten. Daarmee maakt JAX-RS maar één instantie aan, die over verschillende requests wordt hergebruikt.

## 10: Toevoegen foutafhandeling
In voorgaande twee onderdelen heb je enkel de happy-flow geïmplementeerd. Er zijn echter twee situaties
waar het mis kan gaan. Kijk maar naar de code van `ItemService`. In beide gevallen wordt een *unchecked*
`Exception` gegooid, welke uiteindelijk via je REST-Resource van JAX-RS uitkomt. JAX-RS handelt dit verder
af en stuurt een standaard HTTP-Response terug met een foutcode.

Zorg ervoor dat je zelf een specifieke foutcode teruggeeft in beide situaties

* In het geval van een `ItemNotAvailableException` behoor je een HTTP-Statuscode 404 terug te geven.
* In het geval van een `IDAlreadyInUseException` behoor je een HTTP-Statuscode 409 terug te geven.

Misschien is je eerste gedachte dit met een `try...catch` op te lossen. In de `try` roep je de `ItemService`
aan en in de `catch` retourneer je een `Response`-object met de juiste status-code. Dat zal zeker werken, 
maar heeft tot gevolg dat je overal `try...catch` constructies in je code krijgt. Het kan dus aanzienlijk
eleganter.

Gebruik deze resource for het correct implemeteren van de foutafhandeling: [Exception Handling](https://dennis-xlc.gitbooks.io/restful-java-with-jax-rs-2-0-en/cn/part1/chapter7/exception_handling.html)

## 11: Implementeer ook het verwijderen van een *item*
Implementeer tot slot ook het verwijderen van een *item*. De `ItemService` bevat hier al de benodigde methode voor.

* Bedenk via welk pad de Resource te bereiken is
* Bedenk welke HTTP-Methode er gebruikt moet worden
* Vergeet ook de foutafhandeling niet te implementeren (of is dat al gedaan??)
