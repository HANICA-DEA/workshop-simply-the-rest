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
geconfigueerd dat het mogelijk is vanuit de IDE een JavaEE War kunt deployen.

## 1: Aanmaken nieuwe JavaEE 8 project
Gebruik Maven voor het aanmaken van een nieuw JavaEE 8 project. Gebruik hiervoor 
[Java EE 8 Essentials](http://www.adam-bien.com/roller/abien/entry/java_ee_8_essentials_archetype) archetype van
Adam Bien.

Merk op dat dit Archetype spreekt over JakartaEE en Eclipse Microprofile (zie hiervoor ook de POM). JakartaEE is de 'nieuwe'
naam voor JavaEE en Eclipse Microprofile is een uitbreiding daarop. Eclipse Microprofile zullen we verder niet
nodig hebben. Voer hierom de volgende acties uit:

* Verwijder de dependencie op Eclipse Microprofile uit de POM
* Verwijder de klasse `com.airhacks.ping.boundary.PingResource`
* Verwijder het configuratiebetand `microprofile-configuration.properties` uit de de map *resources/META-INF*

Het project bevat nu nog een Java-klasse die we niet nodig zullen hebben. Dit betreft de klasse `JAXRSConfiguration`.

* Verwijder de klasse `JAXRSConfiguration`, inclusief de package waar hij in zit.

Tot slot moeten we de klassen toevoegen die in deze repository te vinden zijn. Dit betreft de volledige `services`
map, inclusief alle submappen en bestanden.

* Kopieëer de map `services` in de package structuur van je project. Zorg ervoor dat alle imports kloppen voor de 
klassen uit `services` en dat je je project kunt compileren.

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
* Deploy je applicatie op TomEE en navigeer via de browser naar de url [http://localhost:8080/health](). 

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
aangegeven

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

## 8: Creëer een REST-Resource voor een enkel *item*
Voeg aan je klasse een nieuwe REST-Resource toe die het mogelijk maakt om een enkele *item*, op basis
van het *id* te retourneren.

* De Resource is te bereiken via het pad `/items/:id`
* De Resource wordt aangesproken middels een GET request
* Het volledige *item* wordt geretourneerd
* De Resource produceert `application/json`
* Implementeer enkel de happy-flow

De vraag is nu vooral hoe om te gaan met de `:id` uit het pad. Raadpleeg daarvoor deze bron: 
[JAX-RS path params](https://www.mkyong.com/webservices/jax-rs/jax-rs-pathparam-example/)


