# Algoritmer, datastrukturer och komplexitet (ADK)
Detta repo innehåller Max Nordlunds och Angelina von Gegerfelts labbar i ADK.
Koden är licenserade under MIT, men vi vill påminna om att inte plagiera även om
licensen skulle tillåta det. MIT kräver dock att man lägger till licensen när
man ska distribuera koden.

## Labb 1: Konkordans
En konkordans är en databas där man kan slå upp ord och då få se alla
förekomster av ordet tillsammans med orden närmast före och närmast efter i
texten. Detta är ett stort hjälpmedel för lingvister som vill undersöka hur
olika ord används i språket.

I denna uppgift ska du skriva ett program som givet en text skapar en
konkordansdatabas och ett program som frågar användaren efter ord, slår upp
ordet och presenterar alla förekomster av ordet i sitt sammanhang. Det är
viktigt att varje sökning går mycket snabbt så det gäller att det första
programmet lagrar konkordansen på ett sådant sätt att det går snabbt att göra en
sökning.

_Exempel:_ på körning av sökprogrammet
```shell
$ java Konkordans komplexiteten
Det finns 7 förekomster av ordet.
ta på scen. Breddningsarbete. Komplexiteten har denna innebörd bland anna
räckvidden, hastigheterna och komplexiteten i omvärlden ökar. Domen inneb
 beter sig misstänkt? Ändå är komplexiteten så hög att jag stundtals blir
ttsplatsen. Vi är medvetna om komplexiteten i denna fråga och ser med oro
n. I det övriga materialet är komplexiteten sedan så stor att en fantasif
 av den från 1928 tilltagande komplexiteten i skattelagstiftningen. De då
ttelseorganisationen CIA. Men komplexiteten hos de föreningar som kemiste
```

### Parprogrammering
Denna labb måste genomföras i tvåpersonsgrupper som arbetar enligt den agila
programutvecklingstekniken parprogrammering. Ni ska också använda verktyget
Parkour som hjälper er att hålla koll på er parprogrammering.
Läs [vad parprogrammering är och hur man använder Parkour][Parkour].

### Krav
Följande krav ställs på din lösning:

* Programmet ska vara skrivet i ett riktigt programspråk och inte något
  operativsystemnära skriptspråk eller liknande.
* Konkordansen ska inte skilja på stora och små bokstäver. Användaren ska alltså
  kunna skriva in alla sökfrågor med små bokstäver.
* Det givna programmet tokenizer.c på kurskatalogen definierar hur texten ska
  delas upp i enskilda ord.
* Konstruktionsprogrammet behöver inte vara jättesnabbt eftersom det bara ska
  köras en gång, men det måste vara någorlunda effektivt så att det kan skapa
  konkordansen på rimlig tid. Det får inte ta mer än två minuter att skapa
  konkordansen på en Ubuntudator i datorsalarna.
* Sökprogrammets utmatning ska inledas med en rad som anger antalet förekomster.
  Därefter ska varje förekomst av ordet presenteras på varje rad med till
  exempel 30 tecken före och 30 tecken efter. Ersätt radbyten med mellanslag.
  Om det finns fler än 25 förekomster bör programmet fråga användaren om hon
  vill ha förekomsterna utskrivna på skärmen.
* Man ska kunna söka efter ett ord, till exempel "bil", genom att i
  terminalfönstret ge kommandot konkordans bil (Om du använt C, C++ eller
  liknande) eller java Konkordans bil (om du använt Java).
  Svaret måste komma inom en sekund på en av skolans Ubuntudatorer.
* Sökprogrammet ska inte läsa igenom hela texten och får inte använda speciellt
  mycket internminne. Internminnesbehovet ska inte växa med antalet distinkta
  ord i den ursprungliga texten. Du ska därför använda latmanshashning (se
  föreläsning 3) som datastruktur.

### Tips

Texten, som ligger på `/info/adk14/labb1/korpus`, är en stor fil och ska inte i
sin helhet läsas in i internminnet under sökningen. Istället bör sökprogrammet
öppna filen och hoppa till dom avsnitt som ska presenteras med seek (använd till
exempel fseek i stdio.h i C eller seek i java.io.RandomAccessFile i Java).
Texten har teckenkodningen ISO-8859-1, som också kallas ISO-Latin 1. Det betyder
att varje tecken lagras i en byte. Du konverterar en bytearray b till String i
Java med new String(b, "ISO-8859-1"). I andra riktningen: en String s
konverteras till en bytearray i ISO-8859-1 med s.getBytes("ISO-8859-1").

Mer information om teckenkonvertering i Java finns [här][Java string]. I C är
konvertering mellan ISO-8859-1 och Unicode-kodningar svårare. Om du använder C
räcker det att sökprogrammet kan användas med teckenkodningen ISO-8859-1.

Ta ingen kopia av textfilen utan låt sökprogrammet använda ursprungstextfilen på
kurskatalogen.

Konstruktionsprogrammet måste skapa något slags index som talar om för varje ord
på vilka positioner i texten det förekommer. Detta index blir av samma
storleksordning som texten och sökprogrammet ska därför inte heller läsa in hela
indexet. Låt det ligga på en fil (eller flera filer) och positionera med hjälp
av seek även i denna fil.

Indexfilerna blir stora och får nog inte plats på din skivminnesarea, så skapa
dom istället på temporärarean `/var/tmp` och ta bort dom när du är klar.

Använd gärna färdiga Unixverktyg som sort vid konstruktionen. En enkel
tokeniserare (ett program som läser en text och plockar ut dom enskilda orden
samt deras position i texten) finns på `/info/adk14/labb1/tokenizer.c`.

Du kan använda en Makefile eller ett shell-skript för att starta flera program
(till exempel tokenizer och sort) när du konstruerar konkordansen. Kommandot som
kör tokenizer och sort kan se ut ungefär så här:
```shell
/info/adk14/labb1/tokenizer < /info/adk14/labb1/korpus | sort > /var/tmp/ut
```
Eftersom Ubuntu normalt använder UTF-8 behöver du sätta shellvariabeln
LC_COLLATE till C (med kommandot `export LC_COLLATE=C` i bash) innan du kör
sort. Detta gör att sort tolkar texten som ISO-8859-1 och därmed sorterar
tecknen i ordningen A B C ... Z a b c ... z Ä Å Ö ä å ö.

Testa ditt program noga. Tänk ut svåra testfall (olika ytterligheter som
enbokstavsord, första eller sista ordet i korpusen eller i indexet etc).

Java är numera ganska snabbt, men just vid filhantering är det viktigt att man
är noggrann när man använder Java. När du skapar konkordansen kommer du troligen
att vilja skriva många gånger på en eller flera filer. Se till att de strömmar
du konstruerar för skrivning (och läsning) är buffrade (läsning och skrivning på
en RandomAccessFile kan inte buffras). Du kan läsa om Javas in- och utmatning i
[Java tutorial][Java file].

### Vid redovisningen
Er labblösning ska redovisas för en labbhandledare vid något av kursens
schemalagda labbpass och vid en av skolans Ubuntudatorer. I kursen används
kösystemet SimaManager (kommandot sm) för att hålla reda på redovisningskön.
Förbered redovisningen genom att ha genererat konkordansen och ha program,
testfall, skisser och labbkvittona redo. Ni kommer då att få redovisa följande:

* Visa i Parkours statistik att ni har parprogrammerat.
* Visa en uppsättning testfall som ni har tagit fram för att kolla att
  programmet gör rätt. Ni ska också kunna motivera varför ni valt just dessa
  testfall.
* Visa att programmet fungerar och är tillräckligt snabbt för era testfall och
  labbhandledarens testfall.
* Visa och förklara hur lösningens datastrukturer på fil och i minnet fungerar.
* Visa programkoden och vara beredd att svara på frågor om den.

Båda i labbgruppen ska kunna svara för hela programmet (vilket blir en naturlig
följd av att ni parprogrammerat).

## Labb 2: Rättstavning

I katalogen `/info/adk14/labb2` finns ett Javaprogram som löser nedanstående
problem. Din uppgift är att snabba upp programmet så att det går ungefär 10000
gånger snabbare. Korrekthet och effektivitet testas genom att din lösning
skickas till [Kattis][]. För att klara labben ska du bli godkänd av [Kattis][]
samt redovisa labben för en handledare. Börja med att logga in i [Kattis][] och
anmäla dig till adk14 i menyalternativet Kurser i översta menyn.

### Problem
Editeringsavståndet mellan två ord är det minimala antalet bokstavsoperationer
som krävs för att transformera det ena ordet till det andra. Det finns tre
tillåtna bokstavsoperationer:

1. ta bort en av bokstäverna i ordet
2. lägg till en bokstav någonstans i ordet
3. byt ut en bokstav i ordet mot en annan bokstav

Till exempel kan ordet alroitm transformeras till algoritm genom att bokstaven r
byts ut mot g (regel 3) och bokstaven r skjuts in efter bokstaven o (regel 2).
Kedjan

alroitm -> algoitm -> algoritm

visar att editeringsavståndet mellan alroitm och algoritm är högst 2. Eftersom
det inte går att transformera alroitm till algoritm i en enda bokstavsoperation
så är editeringsavståndet mellan orden precis 2.

Ett vanligt sätt att ta fram rättstavningsförslag till ett felstavat ord är att
helt enkelt returnera dom ord i ordlistan som har minst editeringsavstånd till
det felstavade ordet. Programmet ska givet en ordlista och ett antal felstavade
ord beräkna rättstavningsförslag på detta sätt.

### Specifikation
Indata består av två delar. Den första delen är ordlistan, som består av ett
antal ord i utf-8-bokstavsordning, ett ord per rad. Denna del avslutas av en rad
som bara innehåller ett '#'-tecken. Den andra delen är ett antal felstavade ord
som ska rättstavas, ett ord per rad. Dom felstavade orden ingår inte i
ordlistan. Varje ord i indata består bara av små bokstäver i svenska alfabetet
(a-z, å, ä, ö), inga mellanslag, skiljetecken eller siffror.

Programmet ska för varje felstavat ord skriva ut en rad bestående av det
felstavade ordet följt av det minimala editeringsavståndet inom parentes följt
av en lista med alla ord i ordlistan som har minimalt editeringsavstånd till det
felstavade ordet. Listan ska vara i bokstavsordning och varje ord i listan ska
föregås av mellanslag. Ordlistan har högst en halv miljon ord och antalet
felstavade ord i indata är högst 100.

### Exempel på körning
En ordlistefil finns i `/info/adk14/labb2/ordlista`. Du kan provköra ditt
program  genom att skriva in några felstavade ord (till exempel labd och
dabbbhud) på  varsin rad i en fil (t.ex. testord.txt) och sedan köra

```shell
spel01$ cat /info/adk14/labb2/ordlista testord.txt | java Main
labd (1) labb lagd land
dabbbhud (4) anbud dabba nabbad
```

### Uppgift
Det givna Javaprogrammet löser visserligen ovanstående problem, men det tar
timmar att få fram svaret. Du ska effektivisera programmet så att det hittar
svaret inom den tidsgräns som [Kattis][] ger.

Bra testfall att testa ditt program med finns på `/info/adk14/labb2/testfall/`

Teoriuppgifterna ger uppslag om olika sätt att effektivisera programmet. Ditt
optimerade program ska ha samma in- och utmatning som det givna programmet och
det måste fortfarande vara Java.

[Kattis][] känner till problemet som [adkspelling][]

## Labb 3: Flöden och matchningar
Du ska i tre steg skriva ett program som får en bipartit graf som indata och
producerar en matchning av maximal storlek som utdata genom att reducera
(transformera) matchningsproblemet till flödesproblemet. Korrekthet och
effektivitet testas genom att lösningarna på de tre stegen skickas till
[Kattis][].  För att klara labben ska du bli godkänd av [Kattis][] på de tre
stegen samt redovisa  labben för en handledare. [Kattis][] kontrollerar både att
programmet gör rätt och  att det löser problemet tillräckligt snabbt. [Kattis][]
klarar av programspråken  Java, C, C++ och Python, men tidskraven i denna labb
gör att vi avråder från  Python.

### Steg 1: Reducera problemet till flödesproblemet
Du ska skriva ett program som löser matchningsproblemet med hjälp av en svart
låda som löser flödesproblemet. Programmet ska fungera enligt denna översiktliga
programstruktur:

* Läs indata för matchningsproblemet från standard input.
* Översätt matchningsinstansen till en flödesinstans.
* Skriv indata för flödesproblemet till standard output (se till att utdata
  flushas).
* Den svarta lådan löser flödesproblemet.
* Läs utdata för flödesproblemet från standard input.
* Översätt lösningen på flödesproblemet till en lösning på matchningsproblemet.
* Skriv utdata för matchningsproblemet till standard output.

Se nedan hur in- och utdataformaten för matchnings- och flödesproblemen ser ut.
Ditt program ska lösa problemet effektivt. [Kattis][] kommer att provköra
programmet  på bipartita grafer på upp till (5000+5000) hörn och upp till 10000
kanter. [Kattis][] känner till problemet som
[oldkattis:adkreducetoflow][adkreducetoflow]. Det finns ett  programskelett för
steg 1 i några olika språk på katalogen `/info/adk14/labb3/exempelprogram`

### Steg 2: Lös flödesproblemet
Nu ska du skriva ett program som löser flödesproblemet. Programmet ska läsa
indata från standard input och skriva lösningen till standard output. Se nedan
hur in- och utdataformaten för flödesproblemet ser ut. Ditt program ska lösa
problemet effektivt. [Kattis][] kommer att provköra programmet  på generella
flödesgrafer på upp till 2000 hörn och 10000 kanter. [Kattis][] känner  till
problemet som [oldkattis:adkmaxflow][adkmaxflow].

### Steg 3: Kombinera steg 1 & 2
I steg 1 löste du matchningsproblemet med hjälp av en lösning till
flödesproblemet. I steg 2 löste du flödesproblemet. Nu ska du kombinera dessa
lösningar till ett enda program genom att byta ut kommunikationen av
flödesinstansen över standard input och standard output till ett funktionsanrop.
Programmet ska fortfarande läsa indata från standard input och skriva lösningen
till standard output.

Ditt program ska lösa problemet effektivt. [Kattis][] kommer att provköra
programmet på bipartita grafer på upp till (5000+5000) hörn och upp till 10000
kanter. [Kattis][] känner till problemet som
[oldkattis:adkbipmatch][adkbipmatch].

### Matchningsproblemet
Givet en bipartit graf G = (X,Y,E) finn en maximal matchning.

### Indata
Den första raden består av två heltal som anger antalet hörn i X respektive Y.
Den andra raden består av ett tal som anger |E|, det vill säga antalet kanter i
grafen. De följande |E| raderna består var och en av två heltal som svarar mot
en kant.

Hörnen numreras från 1 och uppåt. Om man angett a hörn i X och b hörn i Y så
låter vi X = {1, 2,..., a} och Y = {a+1, a+2,..., a+b}. En kant anges med
ändpunkterna (först X-hörnet och sedan Y-hörnet).

_Exempel:_ En graf kan till exempel kodas så här.

Rad | värde
---:|------
  1 | 2 3
  2 | 4
  3 | 1 3
  4 | 1 4
  5 | 2 3
  6 | 2 5

Denna graf har alltså X = {1, 2} och Y = {3, 4, 5}. Kantmängden E innehåller
kanterna (1, 3), (1, 4), (2, 3) och (2, 5).

### Utdata
Först skrivs en rad som är densamma som den första i indata, och därefter en rad
med ett heltal som anger antalet kanter i den funna matchningen. Därefter skrivs
en rad för varje kant som ingår i matchningen. Kanten beskrivs av ett talpar på
samma sätt som i indata.

_Exempel:_ Om vi har grafen ovan som indata så kan utdata se ut så här.

Rad | värde
---:|------
  1 | 2 3
  2 | 2
  3 | 1 3
  4 | 2 5

### Flödesproblemet
Givet en flödesgraf G = (V,E) finn ett maximalt flöde. Lös flödesproblemet med
Edmonds-Karps algoritm, det vill säga Ford-Fulkersons algoritm där den kortaste
stigen hittas med breddenförstsökning.

### Ford-Fulkersons algoritm i pseudokod
c[u,v] är kapaciteten från u till v, f[u,v] är flödet, cf[u,v] är
restkapaciteten.

```
for varje kant (u,v) i grafen do
    f[u,v]:=0; f[v,u]:=0
    cf[u,v]:=c[u,v]; cf[v,u]:=c[v,u]
while det finns en stig p från s till t i restflödesgrafen do
    r:=min(cf[u,v]: (u,v) ingår i p)
    for varje kant (u,v) i p do
         f[u,v]:=f[u,v]+r; f[v,u]:= -f[u,v]
         cf[u,v]:=c[u,v] - f[u,v]; cf[v,u]:=c[v,u] - f[v,u]
```

### Indata
Den första raden består av ett heltal som anger antalet hörn i V.
Den andra raden består av två heltal s och t som anger vilka hörn som är källa
respektive utlopp. Den tredje raden består av ett tal som anger |E|, det vill
säga antalet kanter i grafen. De följande |E| raderna består var och en av tre
heltal som svarar mot en kant.

Hörnen numreras från 1 och uppåt. Om man angett a hörn i V så låter vi V = {1,
2,..., a}. En kant anges med ändpunkterna (först från-hörnet och sedan till-
hörnet) följt av dess kapacitet.

_Exempel:_ En graf kan till exempel kodas så här.

Rad | värde
---:|------
  1 | 4
  2 | 1 4
  3 | 5
  4 | 1 2 1
  5 | 1 3 2
  6 | 2 4 2
  7 | 3 2 2
  8 | 3 4 1

### Utdata
Den första raden består av ett heltal som anger antalet hörn i V.
Den andra raden består av tre heltal s,t, samt flödet från s till t.
Den tredje raden består av ett heltal som anger antalet kanter med positivt
flöde. Därefter skrivs en rad för varje sådan kant. Kanten beskrivs av tre tal
på liknande sätt som i indata, men i stället för kapacitet har vi nu flöde.

_Exempel:_ Om vi har grafen ovan som indata så kan utdata se ut så här.

Rad | värde
---:|------
  1 | 4
  2 | 1 4 3
  3 | 5
  4 | 1 2 1
  5 | 1 3 2
  6 | 2 4 2
  7 | 3 2 1
  8 | 3 4 1

### Testning
I katalogen `/info/adk14/labb3` ligger programmen bipgen, flowgen, maxflow,
combine och matchtest som du kan köra för att testa dina program.

* Programmet bipgen genererar en slumpvis vald bipartit graf. Grafen skrivs på
  _standard output_ på ovan angivet format för indata till matchningsprogrammet.
  ```shell
  /info/adk14/labb3/bipgen x y e
  ```
  ger en graf med x hörn i X, y hörn i Y och e kanter.
* Programmet flowgen genererar en slumpvis vald flödesgraf. Grafen skrivs på
  _standard output_ på ovan angivet format för indata till flödesprogrammet.
  ```shell
  /info/adk14/labb3/flowgen v e c
  ```
  ger en graf med v hörn och e kanter vars kapaciteter är positiva heltal inte
  större än c.
* Programmet maxflow löser flödesproblemet och kan användas som svart låda i
  steg 1. maxflow tar en flödesgraf på standard input och skriver ut ett
  maximalt flöde på _standard output_.
* Programmet combine är ett hjälpprogram som du kan använda dig av i steg 1 för
  att få ditt program att prata med den svarta lådan.
  ```shell
  /info/adk14/labb3/combine java MatchReduce \; /info/adk14/labb3/maxflow <
  graffil > matchfil
  ```
  kommer att köra java MatchReduce som lösning på steg 1, och använda kursens
  maxflow-program som svart låda. Indatagrafen tas från filen graffil och utdata
  skickas till filen matchfil.
* Programmet matchtest läser en graf följt av utdata från ett matchningsprogram
  (alltså, först grafen och sedan matchningen) och kontrollerar att matchningen
  är maximalt stor. Utdata skrivs på standard outputoch kan vara Matchning av
  maximal storlek, Matchning av mindre än maximal storlek eller Ingen matchning.
  Så här kan du använda bipgen och matchtest för att testa din lösning på steg 3
  (minlabb).
  ```shell
  /info/adk14/labb3/bipgen 5000 5000 10000 > graffil  minlabb < graffil >
  matchfil
  cat graffil matchfil | /info/adk14/labb3/matchtest
  ```
* Bra testfall att testa de tre stegen med finns på
  `/info/adk14/labb3/testfall/`

Om du inte vet vad tecknen `>`, `<` och `|` betyder i exemplen ovan så kan du
titta i Unixhäftet eller fråga en labbhandledare. För att kolla hur lång tid
ditt program kör på dina egna testfall kan du använda kommandot time och titta
på user time.

## Labb 4: NP-fullständighetsreduktioner - Rollbesättning
Ansvarig för castingen på ett filmbolag behöver koppla ihop rätt skådespelare
med rätt roller. Samma person kan spela flera roller, men samma roll kan endast
innehas av en person. Manus anger vilka roller som är med i samma scener. Inga
monologer får förekomma. Varje skådespelare får bara ha en roll i varje scen.

Dessutom är divorna p1 och p2 garanterade vid varje castingtillfälle. Detta
medför extraarbete eftersom de b åda inte tål varandra och rollerna ska besättas
så att de aldrig spelar mot varandra. Rollbesättningsproblemet är att avgöra
ifall alla roller kan besättas med de skådespelare som finns till hands.

Ingående parametrar är alltså:
Roller r1, r2,... , rn
Skådespelare p1, p2,... ,pk
Villkor typ 1 (till varje roll): rt kan besättas av p1, p2, p6
Villkor typ 2 (till varje scen): i su medverkar r1, r3, r5, r6 och r7

### Indataformat
Rad ett består av tre tal: n, s och k (antal roller, antal scener och antal
skådespelare, n≥1, s≥1, k≥2).

De följande n raderna representerar villkoren av typ 1 och börjar med ett tal
som anger antalet efterföljande tal på raden, följt av de möjliga skådespelarnas
nummer (mellan 1 och k, kursiverade i exemplen nedan).

De sista s raderna är villkor av typ 2 och börjar ett tal som anger antalet
efterföljande tal på raden, följt av tal som representerar de olika rollerna som
är med i respektive scen. Varje roll förekommer högst en gång på varje sådan
rad, så antalet roller på en rad ligger mellan 2 och n.

Fråga: Kan rollerna besättas med högst k st skådespelare så att p1 och p2 deltar
men inte är med i samma scener som varandra?
_Exempel:_ på godkända indata

nej-instans: | ja-instans:
-------------|------------
5 5 3        | 6 5 4
3 1 2 3      | 3 1 3 4
2 2 3        | 2 2 3
2 1 3        | 2 1 3
1 2          | 1 2
3 1 2 3      | 4 1 2 3 4
2 1 2        | 2 1 4
2 1 2        | 3 1 2 6
3 1 3 4      | 3 2 3 5
2 3 5        | 3 2 4 6
3 2 3 5      | 3 2 3 6
             | 2 1 6

### Uppgift
I den här laborationen ska du visa att rollbesättningsproblemet är NP-svårt
genom att reducera ett känt NP-fullständigt problem, som finns inlagt i
[Kattis][].  Din reducerade instans kommer att granskas och lösas av [Kattis][].
Du får välja  mellan att reducera problemen Graffärgning (problem-id:
[oldkattis:adkreduction1][adkreduction1])  och Hamiltonsk cykel
[oldkattis:adkreduction2][adkreduction2]). Indataformat för dessa problem
beskrivs nedan. Din uppgift är alltså att implementera en reduktion, inte att
lösa problemet.

[Kattis][] testar om din reduktion är korrekt, men du måste naturligtvis kunna
bevisa att den är det vid redovisningen. [Kattis][] svar är egentligen avsedda
att  vägleda dig i arbetet med beviset och påpeka om du glömt något viktigt
specialfall. Vid redovisningen kommer handledaren också att fråga varför
problemet ligger i NP och vad komplexiteten är för din reduktion.

Vid rättningen utnyttjas en lösare för instanser av ett (annat) NP-fullständigt
problem inom rimliga storleksgränser. Av tekniska skäl har [Kattis][] en maximal
tillåten storlek på instanserna. Du får bara meddelanden om den ifall du skickar
in en för stor instans. Du får redovisa din reduktion om du kan bevisa att den
är korrekt, oavsett om [Kattis][] har godkänt den eller inte.

### Graffärgning
Indata: En oriktad graf och ett antal färger m. Isolerade hörn och dubbelkanter
kan förekomma, inte öglor.

Fråga: Kan hörnen i grafen färgas med högst m färger så att inga grannar har
samma färg?

Indataformat:
Rad ett: tal V (antal hörn, V≥1)
Rad två: tal E (antal kanter, E≥0)
Rad tre: mål m (maxantal färger, m≥1)
En rad för varje kant (E stycken) med kantens ändpunkter (hörnen numreras från 1
till V)
### Hamiltonsk cykel
Indata: En riktad graf.
Fråga: Finns det en tur längs kanter i grafen som börjar och slutar på samma
ställe och som passerar varje hörn exakt en gång?

Indataformat:
Rad ett: tal V (antal hörn, V≥1)
Rad två: tal E (antal kanter E≥0)
En rad för varje kant (E stycken) med kantens starthörn och sluthörn (hörnen
numreras från 1 till V)

[Parkour]: http://www.csc.kth.se/tcs/projects/cerise/parprogrammering/
[Java file]: http://download.oracle.com/javase/tutorial/essential/io/fileio.html
[Java string]: http://download.oracle.com/javase/tutorial/i18n/text/string.html
[Kattis]: https://kth.kattis.scrool.se/
[adkspelling]: https://kth.kattis.scrool.se/problems/adkspelling
[adkreducetoflow]: https://kth.kattis.scrool.se/problems/oldkattis:adkreducetoflow
[adkmaxflow]: https://kth.kattis.scrool.se/problems/oldkattis:adkmaxflow
[adkbipmatch]: https://kth.kattis.scrool.se/problems/oldkattis:adkbipmatch
[adkreduction1]: https://kth.kattis.scrool.se/problems/oldkattis:adkreduction1
[adkreduction2]: https://kth.kattis.scrool.se/problems/oldkattis:adkreduction2
