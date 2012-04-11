Base64.java:
pezzo di codice per codificare e decodificare in base64 preso da qualche parte sul net. Utile nelle negoziazioni SASL (e forse anche in quelle TLS; tuttavia non sono ancora implementate, essendo opzionali [seppur preferibili]).

XMPPThreadedCommander.java:

Versione corretta e migliorata di XMPPCommander che si basa sull'utilizzo di un thread per la lettura da socket.

StreamReaderThread.java:

Classe di utilità che funziona da thread di lettura sul socket. Implementa alcuni metodi essenziali per l'autenticazione via SASL con XMPP.