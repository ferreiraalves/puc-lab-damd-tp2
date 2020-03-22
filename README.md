# puc-lab-damd-tp2

https://github.com/ferreiraalves/puc-lab-damd-tp2

## Dependências 
- RabbitMQ
- Jackson 

## Instruções de utilização
- Importe o repositório como um projeto Maven utilizando sua IDE de preferência.
- Altere as variáveis de configuração (utils/Configurations.java) de acordo com sua preferência.
- Inicialize o servidor da bola de valores através do arquivo BolsaServer.java



## Exemplo de utilização

O arquivo Main.java possui um exemplo de utilização do sistema. Este exemplo é semelhante ao diagrama de sequência apresentado na proposta do TP. Ou seja, nele, o cliente lança uma ordem de compra, através de um tópico pré-definido (default:BROKERS).

Esta ordem de compra é recebida pelo Servidor da Bolsa, assinante de todas as mensagens do tópico, processada e armazenada. Com este processo completo, o servidor envia um call-back para o tópico de origem e envia uma mensagem para um segundo tópico(default: BOLSADEVALORES). No caso, Brokers podem ser assinantes deste segundo tópico para receber informações sobre ativos ou operações de interesse.


Em seguida o exemplo envia uma ordem de venda para o servidor. Como o valor da venda é inferior ao valor da compra, uma transação é inicializada. Esta transmissão é armazenada, a ordem de compra é removida da lista de oferta disponíveis e uma mensagem é enviada ao tópico BOLSADEVALORES para notificar os Brokers assinantes.


Para o exemplo, iremos criar uma assinatura para nosso Broker de forma que o mesmo receberá todas as mensagens do tópico BOLSADEVALORES `*.*`. No entanto, poderíamos criar assinaturas para receber apenas tópicos específicos no formato `operação.ativo` . Por exemplo, caso a assinatura tivesse o valor "transaction.*" receberíamos todas as mensagens do tipo `transaction`. Um valor de `*.BBDC4` no traria todas as operações do ativo `BBDC4`.


## Output

A execução do arquivo de exemplo gera a seguinte saida:
### Client
```
 [x] Sent 'compra.BBDC4':'{"quant":2,"val":3.5,"corretora":"Hugo"}'
 [x] Sent 'venda.BBDC4':'{"quant":2,"val":2.5,"corretora":"Jonas"}'
 [x] Received 'compra.BBDC4:{"quant":2,"val":3.5,"corretora":"Hugo"}'
 [x] Received 'transaction.BBDC4:{"id":"c997fcb3-afa6-4c5a-9b49-a0a94976aaf8","compra":{"quant":2,"val":3.5,"corretora":"Hugo"},"venda":{"quant":2,"val":2.5,"corretora":"Jonas"}}'
```
### Server 
```
 [*] Waiting for messages. To exit press CTRL+C
 [x] Received 'compra.BBDC4	{"quant":2,"val":3.5,"corretora":"Hugo"}'
[INFO] NEW COMPRA: {"quant":2,"val":3.5,"corretora":"Hugo"}
 [x] Sent 'compra.BBDC4':'{"quant":2,"val":3.5,"corretora":"Hugo"}'
 [x] Received 'venda.BBDC4	{"quant":2,"val":2.5,"corretora":"Jonas"}'
[INFO] REMOVENDO COMPRA: {"quant":2,"val":3.5,"corretora":"Hugo"}
[INFO] NEW TRANSACTION: {"quant":2,"val":3.5,"corretora":"Hugo"}{"quant":2,"val":2.5,"corretora":"Jonas"}
 [x] Sent 'transaction.BBDC4':'{"id":"c997fcb3-afa6-4c5a-9b49-a0a94976aaf8","compra":{"quant":2,"val":3.5,"corretora":"Hugo"},"venda":{"quant":2,"val":2.5,"corretora":"Jonas"}}'
```

Ou seja, o servidor recebe a ordem de compra e responde com a confirmação da ordem de compra. Em seguida recebe a  ordem de venda, o que fecha uma transação. Esta transação e processada e o servidor lança uma notificação, que é recebida pelo broker.

## Classes
Operações relevantes serão descritas para cada classe, caso necessário.

### operations
- Compra: Representa uma compra.
- Transaction: Representa uma transação bem-sucedida.
    - notifyBolsaQueue(): Envia uma notificação de transação bem sucedida para ser o tópico correspondente.
- Venda: Representa uma venda.

### operators
- BolsaClient: responsável pela lógica e execução das regras de negócio da Bolsa de Balores
    - processCompra(), processVenda(): recebe uma mensagem e transforma em objeto. Verifica se existe uma operação compatível para efetuar uma transação. Caso haja, inicializa a transação. Caso não haja, armazena a ordem de operação.
    - notifyBolsaQueue(): Envia uma notificação de ordem de operação para o tópico correspondente.
- Broker: responsável pela lógica e execução das regras de negócio de Brokers. Deve ser inicializado com um nome de exchange para envio de mensagens.
    - sendMsg(): Envia uma mensagem com uma operação de compra ou venda para o servidor da bolsa de valores.
    - subscribe(): Assina o Broker em um novo topico. Essa função interrompe a tread que recebe mensagens para este broker, atualiza as configuração com os novos valores recebidos e inicia a thread uma nova thread.
- Offers: responsável por armazenar e gerenciar as ofertas de compra e vendas no sistema.
     -  add(): Recebe uma operação e armazena em uma lista.
     -  checkMatchingOffer(): Recebe uma operação e verifica se existe uma operação correspondente no sistema para que uma Transação possa ser inicializada.
    - Remove(): Remove uma operação do sistema.
- Transactions: Responsável por manter o registro de transações no sistema.

### utils
- Configurations: Gerencia as configurações do projeto. São elas: 
    - host: O servidor no qual a aplicação deve rodar. No caso de servidor remoto, determina o destino das requisições enviadas pelo client. 
    - bolsaExchange: Nome do exchange de destino das mensagens geradas pela bolsa de valores.
    - brokerExchange: Nome do exchange de destino das mensagens geradas pelos brokers. 
- CSVReader: Lê o arquivo .CSV fornecido para determinar os ativos válidos no sistema.
- SubscriverRunnable: Configuração da thread responsável pela rececpção de mesnagens por lado do Client. 

### src
- BolsaServer: Inicializa o servidor da bolsa de valores. Encaminha as mensagens de acordo com a operação recebida.
     - deliverCallback(): Realiza o callback para a mensagem recebida, evitando que ela seja lida novamente.