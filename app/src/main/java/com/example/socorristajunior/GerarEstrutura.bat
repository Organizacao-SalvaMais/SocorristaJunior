@echo off
REM Esta linha desativa a exibicao dos proprios comandos no console.

REM Define o nome do arquivo de saida que sera gerado.
set "NOME_ARQUIVO=estrutura_completa (com_arquivos).txt"

REM Informa ao usuario que o processo esta iniciando.
echo Gerando a arvore de diretorios E arquivos...

REM Executa o comando 'tree' para mapear pastas E arquivos.
REM /A - Usa caracteres ASCII (para compatibilidade com .txt).
REM /F - (Files) Inclui os nomes dos arquivos dentro de cada pasta.

REM CORRECAO: A saida e redirecionada para "%NOME_ARQUIVO%" (com aspas).
REM Isso força o sistema a tratar 'estrutura_completa (com_arquivos).txt'
REM como um nome de arquivo unico, impedindo que os parenteses '()' causem o erro.
tree /A /F > "%NOME_ARQUIVO%"

REM Informa ao usuario que o arquivo foi criado com sucesso.
echo Arquivo '%NOME_ARQUIVO%' foi gerado com sucesso!

REM Pausa o script e espera o usuario pressionar uma tecla.
pause