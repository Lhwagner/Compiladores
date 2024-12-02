package teste;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class Semantico implements Constants {

	private static final String float64 = "float64";
	private static final String int64 = "int64";
	private static final String bool = "bool";
	private static final String string = "string";
	private String operador;
	int contadorRotulo = 1;
	private StringBuilder codigo = new StringBuilder();
	private Stack<String> pilha_tipos = new Stack<>();
	private Stack<String> pilha_rotulos = new Stack<>();
	private ArrayList<String> lista_id = new ArrayList<>();	
	private HashMap<String, String> tabela_simbolos = new HashMap<>();
	


	public void executeAction(int action, Token token) throws SemanticError {
		switch (action) {
		case 100:
			codigo.append("	.assembly extern mscorlib {}\n" 
					+ "	.assembly _codigo_objeto{}\n"
					+ "	.module   _codigo_objeto.exe\n" 
					+ " .class public _UNICA{\n"
					+ "	.method static public void _principal() {\n" 
					+ "	.entrypoint").append("\n");
			break;
		case 101:
			codigo.append("		ret\n" 
							+ "		}\n" 
							+ "	}\n").append("\n");
			break;
		case 102:
			acao102(token);
			break;
		case 103:
			acao103();
			break;
		case 104:
			lista_id.add(token.getLexeme());
			break;
		case 105:
			acao105(token);
			break;
		case 106:
		    codigo.append("ldstr ").append(token.getLexeme()).append("\n");
		    codigo.append("call void [mscorlib]System.Console::Write(string)").append("\n");
		    break;
		case 107:
			codigo.append("\n");
			break;
		case 108:
			String tipo14 = pilha_tipos.pop();
			if (tipo14.equalsIgnoreCase("int64")) {
				codigo.append("conv.i8").append("\n");
			}
			codigo.append("call void [mscorlib]System.Console::Write(" + tipo14 + ")").append("\n");
			break;
		case 109:
			acao109();
			break;
		case 110:
			acao110();
			break;
		case 111:
			acao111();
			break;
		case 112:
            acao112();
            break;
		case 113:
			acao113();
			break;
		case 114:
			acao114();
			break;
		case 115:
            acao115();
            break;
		case 116:
			bool();
			codigo.append("and").append("\n");
			break;
		case 117:
			bool();
			codigo.append("or").append("\n");
			break;
		case 118:
			pilha_tipos.push(bool);
			codigo.append("ldc.i4.1").append("\n");
			break;
		case 119:
			pilha_tipos.push(bool);
			codigo.append("ldc.i4.0").append("\n");
			break;
		case 120:
			codigo.append("ldc.i4.1").append("\n");
			codigo.append("xor").append("\n");
			break;
		case 121:
			operador = token.getLexeme();
			break;
		case 122:
			acao122();
			break;
		case 123:
			addSubMul();
			codigo.append("add").append("\n");
			break;
		case 124:
			addSubMul();
			codigo.append("sub").append("\n");
			break;
		case 125:
			addSubMul();
			codigo.append("mul").append("\n");
			break;
		case 126:
			addSubMul();
			codigo.append("div").append("\n");
			break;
		case 127:
			acao127(token);
			break;
		case 128:
			pilha_tipos.push(int64);
			codigo.append("ldc.i8 ").append(token.getLexeme().replaceAll("\\.", "")).append("\n");
			codigo.append("conv.r8").append("\n");
			break;
		case 129:
			pilha_tipos.push(float64);
			codigo.append("ldc.r8 ").append(token.getLexeme().replaceAll("\\.", "").replaceAll(",", ".")).append("\n");
			break;
		case 130:
			pilha_tipos.push("string");
			codigo.append("ldstr " + token.getLexeme()).append("\n");
			break;
		case 131:
			codigo.append("ldc.i8 -1").append("\n");
			codigo.append("conv.r8").append("\n");
			codigo.append("mul").append("\n");
			break;
		default:
			System.out.println("Ação #" + action + ", Token: " + token);
			break;
		}
	}
	
	private void acao102(Token token) throws SemanticError {
	    for (String identificador : lista_id) {
	        if (tabela_simbolos.containsKey(identificador)) {
	            throw new SemanticError(" Identificador \"" + identificador + "\" já declarado ");
	        } else {
	            String tipo = getTipoVariavel(identificador);
	            tabela_simbolos.put(identificador, tipo);
	            codigo.append(".locals (").append(tipo).append(" ").append(identificador).append(")").append("\n");
	        }
	    }
	    lista_id.clear();
	}

	private void acao103() throws SemanticError {

	String tipoVariavel = lista_id.get(lista_id.size() - 1);

	for (int i = 0; i < lista_id.size() - 1; i++) {
		codigo.append("dup").append("\n");
	}

	for (String identificador : lista_id) {
		String tipo = validaString(tabela_simbolos.get(identificador));
		if (tipo.isBlank()) {
			codigo.append(".locals(" + getTipoVariavel(tipoVariavel) + " " + identificador + ")");
			tabela_simbolos.put(identificador, getTipoVariavel(tipoVariavel));
		} else if (tipo.equals("float64") && getTipoVariavel(tipoVariavel).equals("int64")) {
			codigo.append("stloc " + identificador + "\n");
		} else if (getTipoVariavel(tipoVariavel).equals(tipo)) {
			if (tipo.equals("int64")) {
				codigo.append("conv.i8").append("\n");
			}
			codigo.append("stloc " + identificador + "\n");
		} else {
			throw new SemanticError(" tipos incompatíveis em comando de atribuição");
		}
	}
	lista_id.clear();
}
	
	private void acao105(Token token) throws SemanticError {
		if (!tabela_simbolos.containsKey(token.getLexeme())) {
	        throw new SemanticError(" Identificador \"" + token.getLexeme() + "\" não declarado");
	    } else {
	        String tipo = tabela_simbolos.get(token.getLexeme());

	        switch (tipo) {
	            case int64:
	                codigo.append("call string [mscorlib]System.Console::ReadLine()\n");
	                codigo.append("call int64 [mscorlib]System.Int64::Parse(string)\n");
	                break;

	            case float64:
	                codigo.append("call string [mscorlib]System.Console::ReadLine()\n");
	                codigo.append("call float64 [mscorlib]System.Double::Parse(string)\n");
	                break;

	            case string:
	                codigo.append("call string [mscorlib]System.Console::ReadLine()\n");
	                break;

	            case bool:
	                codigo.append("call string [mscorlib]System.Console::ReadLine()\n");
	                codigo.append("call bool [mscorlib]System.Boolean::Parse(string)\n");
	                break;

	            default:
	                throw new SemanticError(" Tipo desconhecido para o identificador \"" + token.getLexeme() + "\".");
	        }
	        codigo.append("stloc ").append(token.getLexeme()).append("\n");
	    }
	}
	
	private void acao109() {
	    String novoRotulo1 = "r" + contadorRotulo++;
	    pilha_rotulos.push(novoRotulo1);
	    String novoRotulo2 = "r" + contadorRotulo++;
	    codigo.append("brfalse ").append(novoRotulo2).append("\n");
	    pilha_rotulos.push(novoRotulo2);
	}

	private void acao110() {
	    String rotuloDesempilhado2 = pilha_rotulos.pop(); 
	    String rotuloDesempilhado1 = pilha_rotulos.pop(); 


	    codigo.append("br ").append(rotuloDesempilhado1).append("\n");
	    pilha_rotulos.push(rotuloDesempilhado1);
	    codigo.append(rotuloDesempilhado2).append(":\n");
	}
	
	private void acao111() {
	    String rotuloDesempilhado = pilha_rotulos.pop();
	    codigo.append(rotuloDesempilhado + ":" + "\n");
	}
	
	private void acao112() {
	    String novoRotulo = "r" + contadorRotulo;
	    contadorRotulo++; 
	    codigo.append("brfalse ").append(novoRotulo).append("\n");  
	    pilha_rotulos.push(novoRotulo);  
	}
	
	private void acao113() {
	    String rotulo = "r" + contadorRotulo;
	    codigo.append(rotulo + ":" + "\n");  
	    pilha_rotulos.push(rotulo);  
	    contadorRotulo++; 
	}
	
	private void acao114() {
	    String rotuloDesempilhado = pilha_rotulos.pop();
	    codigo.append("brtrue ").append(rotuloDesempilhado).append("\n");
	}

	private void acao115() {
	    String rotuloDesempilhado = pilha_rotulos.pop();
	    codigo.append("brfalse ").append(rotuloDesempilhado).append("\n");
	}

	private void acao122() {
		String tipo1 = pilha_tipos.pop();
		String tipo2 = pilha_tipos.pop();
		pilha_tipos.push("bool");
		if (operador.equalsIgnoreCase(">")) {
			codigo.append("cgt").append("\n");
		} else if (operador.equalsIgnoreCase(">=")) {
			codigo.append("clt").append("\n");
			codigo.append("ldc.i4 0").append("\n");
			codigo.append("ceq").append("\n");
		} else if (operador.equalsIgnoreCase("<")) {
			codigo.append("clt").append("\n");
		} else if (operador.equalsIgnoreCase("<=")) {
			codigo.append("cgt").append("\n");
			codigo.append("ldc.i4 0").append("\n");
			codigo.append("ceq").append("\n");
		} else if (operador.equalsIgnoreCase("==")) {
			if (tipo1 == "string") {
				codigo.append("call int32 [mscorlib]System.String::Compare(string, string)").append("\n");
			} else {
				codigo.append("ceq").append("\n");
			}
		} else if (operador.equalsIgnoreCase("!=")) {
			codigo.append("ceq").append("\n");
			codigo.append("ldc.i4 0").append("\n");
			codigo.append("ceq").append("\n");
		}
	}

	private void acao127(Token token) throws SemanticError {
		String identificador = token.getLexeme();
		if (validaString(tabela_simbolos.get(identificador)).isBlank()) {
			throw new SemanticError(" Identificador " + identificador + " não declarado");
		}
		codigo.append("ldloc " + token.getLexeme() + "\n");
		String tipo = tabela_simbolos.get(identificador);
		if (tipo.equals("int64") ) {
			codigo.append("conv.r8" + "\n");
		}
		pilha_tipos.push(tipo);
	}

	

	private void bool() {
		pilha_tipos.pop();
		pilha_tipos.pop();
		pilha_tipos.push("bool");
	}

	private void addSubMul() {
		String tipo1 = pilha_tipos.pop();
		String tipo2 = pilha_tipos.pop();
		if (tipo1.contentEquals(float64) || tipo2.contentEquals(float64)) {
			pilha_tipos.push(float64);
		} else {
			pilha_tipos.push(int64);
		}
	}

	private String validaString(Object value) {
		if (value == null) {
			return "";
		} else {
			return (String) value;
		}
	}
	
	private String getTipoVariavel(String value) {
	    if (value.startsWith("i_")) {
	        return int64; 
	    } else if (value.startsWith("f_")) {
	        return float64; 
	    } else if (value.startsWith("s_")) {
	        return string; 
	    } else if (value.startsWith("b_")) {
	        return bool; 
	    } else {
	        throw new IllegalArgumentException("Identificador desconhecido ou tipo não reconhecido: " + value);
	    }
	}


	public StringBuilder getCodigo() {
		return codigo;
	}
}