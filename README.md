# Intellij Idea IDE stub method argument plugin for Java
Intelij IDEA plugin for java, enables you to generate stub method arguments.

While writing tests, often arises the need to create object with fake parameters. In this case you have to type stub parameters of the 
constructor manually.This becomes annoying if you have to create a lot of sub-arguments manually. 

This plugin enables you to generate stub argument values for you methods or constructor calls.
Currently supported all primitives, String and BigDecimal.
See the generated values for each supported data type below.
| type       | value              |
|------------|--------------------|
| byte       | 1                  |
| short      | 1                  |
| int        | 1                  |
| long       | 1L                 |
| float      | 1.0F               |
| double     | 1.0                |
| char       | 'c'                |
| String     | "testArgumentName" |
| BigDecimal | 1                  |
| Object     | null               |

## Usage
1. Place caret to inside the method call(between brackets)
2. Press Alt + Enter
3. In the intentions pop up select: "Generate stub arguments"

![Demo](demo.png?raw=true)
