# Compiler

Compilador de exemplo em Kotlin com análise léxica, sintática e semântica para matéria de Compiladores.

## Executando

```
$ ./gradlew build
$ java -jar build/libs/compiler-1.0.jar example.txt
```


## Definição

```
programa          ::= program ident <corpo> .
corpo             ::= <dc> begin <comandos> end
<dc>              ::= <dc_v> <mais_dc> | <dc_p> <mais_dc> | λ
<mais_dc>         ::= ; <dc> | λ
<dc_v>            ::= var <variaveis> : <tipo_var>
<tipo_var>        ::= real | integer
<variaveis>       ::= ident <mais_var>
<mais_var>        ::= , <variaveis> | λ
<dc_p>            ::= procedure ident <parametros> <corpo_p>
<parametros>      ::= (<lista_par>) | λ
<lista_par>       ::= <variaveis> : <tipo_var> <mais_par>
<mais_par>        ::= ; <lista_par> | λ
<corpo_p>         ::= <dc_loc> begin <comandos> end
<dc_loc>          ::= <dc_v> <mais_dcloc> | λ
<mais_dcloc>      ::= ; <dc_loc> | λ
<lista_arg>       ::= (<argumentos>) | λ
<argumentos>      ::= ident <mais_ident>
<mais_ident>      ::= ; <argumentos> | λ
<pfalsa>          ::= else <comandos> | λ
<comandos>        ::= <comando> <mais_comandos>
<mais_comandos>   ::= ; <comandos> | λ
<comando>         ::= read (<variaveis>) |
                      write (<variaveis>) |
                      while <condicao> do <comandos> $ |
                      if <condicao> then <comandos> <pfalsa> $ |
                      ident <restoIdent>
<restoIdent>      ::= := <expressao> | <lista_arg>
<condicao>        ::= <expressao> <relacao> <expressao>
<relacao>         ::= = | <> | >= | <= | > | <
<expressao>       ::= <termo> <outros_termos>
<op_un>           ::= + | - | λ
<outros_termos>   ::= <op_ad> <termo> <outros_termos> | λ
<op_ad>           ::= + | -
<termo>           ::= <op_un> <fator> <mais_fatores>
<mais_fatores>    ::= <op_mul> <fator> <mais_fatores> | λ
<op_mul>          ::= * | /
<fator>           ::= ident | numero_int | numero_real | (<expressao>)
```

Essa definição pode criar uma linguagem que interpreta o seguinte código:
```
program teste

/* declaracao de variaveis */
var a,b,c: integer;
var d,e,f: real;
var g,h : integer;

/* declaracao de procedimentos */
procedure um (a, g: real; d, c: integer)
  var h, i, j: real;
  var l: integer
begin
  h := 2.0;
  a := g + 3.4 / h;
  l := c - d * 2;
  if (c+d)>=5 then
    write(a)
  else
    write(l)
  $
end;

procedure dois (j: integer; k: real; l: integer)
  var cont,quant: integer
begin
  read(quant);
  while cont <= quant do
     write(cont)
  $;
  l := l + j + cont;
  write(k);
  write(l)
end

/*  corpo * principal / */

begin
read(e); {real}
read(f); {real}
read(g); {inteiro}
read(h); { inteiro} 
d := e/f; {real}
dois(h;d;h);
um(f;e;g;h)         {real,real,inteiro,inteiro}
{aqui termina o programa}
end.
```
## Grafos sintáticos
![Grafos Sintáticos](syntax_diagram.png)