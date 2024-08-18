# Progetto di tecnologie informariche per il web 2023
La specifica richiede di implementare il sito secondo due modalità: [pureHTML](https://github.com/Chri060/tiw-2023-rossi-sharoubim/tree/main/Pure%20HTML) e [RIA](https://github.com/Chri060/tiw-2023-rossi-sharoubim/tree/main/Rich%20Internet%20Application).

## _Specifica_
**Pure HTML** <br>
Si richiede si sviluppare un'applicazione web per la gestione di aste online.
Gli utenti accedono tramite login e possono vendere e acquistare all’asta.
La _home_ contiene due link, uno per accedere alla pagina vendo e uno per accedere alla pagina acquisto. <br>
La pagina _vendo_ mostra una lista delle aste create dall’utente e non ancora chiuse, 
una lista delle aste da lui create e chiuse e due form, una per creare un 
nuovo articolo e una per creare una nuova asta per vendere gli articoli dell’utente. 
Cliccando su un’asta compare una pagina di _dettaglio_ che riporta per un’asta aperta tutti i dati 
dell’asta e la lista delle offerte. Un bottone chiudi permette all’utente di chiudere l’asta se è giunta l’ora della scadenza. 
Se l’asta è chiusa, la pagina riporta tutti i dati dell’asta,il nome dell’aggiudicatario, il 
prezzo finale e l’indirizzo di spedizione dell’utente. <br>
La pagina _acquisto_ contiene una form di ricerca per parola chiave. 
Quando l’acquirente invia una parola chiave la pagina è aggiornata e mostra un elenco di aste aperte per cui la parola
chiave compare nel nome o nella descrizione di almeno uno degli articoli dell’asta.
Cliccando su un’asta aperta compare la pagina _offerta_ che mostra i dati degli articoli, l’elenco delle offerte pervenute 
e un campo di input per inserire la propria offerta. La pagina acquisto contiene
anche un elenco delle offerte aggiudicate all’utente con i dati degli articoli e il prezzo finale. 

**Rich Internet Application**  <br>
Oltre alla specifica precedente si richiede di implementare: 
* Dopo il login, l’intera applicazione è realizzata con un’unica pagina.
* Se l’utente accede per la prima volta l’applicazione mostra il contenuto della pagina acquisto. 
  Se l’utente ha già usato l’applicazione, questa mostra il contenuto della pagina vendo se 
  l’ultima azione dell’utente è stata la creazione di un’asta; altrimenti mostra il contenuto della 
  pagina acquisto con l’elenco delle aste su cui l’utente ha cliccato in 
  precedenza e che sono ancora aperte. L’informazione dell’ultima azione compiuta e delle aste 
  visitate è memorizzata a lato client per la durata di un mese.
* Ogni interazione dell’utente è gestita senza ricaricare completamente la pagina, ma produce 
  l’invocazione asincrona del server e l’eventuale modifica solo del contenuto da aggiornare a 
  seguito dell’evento.

## _Documentazione_
La presentazione contenente la documentazione del progetto si trova al seguente [link](https://github.com/Chri060/tiw-2023-rossi-sharoubim/blob/main/deliverables/Progetto%20TIW%202023.pdf).
