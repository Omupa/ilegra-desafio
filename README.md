# Processador de arquivos

Este projeto realiza leitura e processamento de arquivos em determinada pasta, 
gerando outro arquivo de saída (para cada entrada) com alguns dados extraidos. 
O processamento é realizado para arquivos já existentes e os novos que entrarem 
dentro da pasta de INPUT. 

### Requisitos técnicos
1. Java JDK 8+
2. Maven
3. Passar como variável de ambiente o caminho da pasta de INPUT e o caminho da pasta
de OUTPUT

### Desafios encontrados no desenvolvimento
1. Tentativa de processar arquivo que não está disponível para leitura (lock)
2. Velocidade de processamento em arquivos grandes

### Problemas não resolvidos
1. Salvar dados do arquivo que está sendo processado em memória: O ideal é salvar tais
dados de outra forma, como exemplo: banco de dados.
2. Enquanto está processando os arquivos já existentes na pasta de INPUT o sistema perde 
os novos arquivos de entrada pois o WatchService ainda não foi iniciado: Uma solução é iniciar
 o WatchService na paste de INPUT de forma assincrona.
