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