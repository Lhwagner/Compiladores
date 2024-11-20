package teste;

import javax.swing.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.border.BevelBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class Interface extends JFrame {

    public static JTextArea editor;
    private JTextArea mensagens;
    private JLabel barraStatus;
    private JTextArea lineNumbers;
    private File arquivoAtual; // Variável para armazenar o arquivo atualmente aberto

    public Interface() {

        // Inicializando a variável de arquivo atual como null
        arquivoAtual = null;

        // Configuração da Janela Principal
        setTitle("Compilador");
        setSize(910, 600);
        setMinimumSize(new Dimension(1100, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Barra de Ferramentas
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);

        JButton btnNovo = new JButton("Novo [Ctrl-N]");
        JButton btnAbrir = new JButton("Abrir [Ctrl-O]");
        JButton btnSalvar = new JButton("Salvar [Ctrl-S]");
        JButton btnCopiar = new JButton("Copiar [Ctrl-C]");
        JButton btnColar = new JButton("Colar [Ctrl-V]");
        JButton btnRecortar = new JButton("Recortar [Ctrl-X]");
        JButton btnCompilar = new JButton("Compilar [F7]");
        JButton btnEquipe = new JButton("Equipe [F1]");

        btnNovo.setIcon(new ImageIcon(Interface.class.getResource("/icones/novo.png")));
        btnAbrir.setIcon(new ImageIcon(Interface.class.getResource("/icones/abrir.png")));
        btnSalvar.setIcon(new ImageIcon(Interface.class.getResource("/icones/salvar.png")));
        btnCopiar.setIcon(new ImageIcon(Interface.class.getResource("/icones/copiar.png")));
        btnColar.setIcon(new ImageIcon(Interface.class.getResource("/icones/colar.png")));
        btnRecortar.setIcon(new ImageIcon(Interface.class.getResource("/icones/recortar.png")));
        btnCompilar.setIcon(new ImageIcon(Interface.class.getResource("/icones/compilar.png")));
        btnEquipe.setIcon(new ImageIcon(Interface.class.getResource("/icones/equipe.png")));

        toolbar.add(btnNovo);
        toolbar.add(btnAbrir);
        toolbar.add(btnSalvar);
        toolbar.addSeparator();
        toolbar.add(btnCopiar);
        toolbar.add(btnColar);
        toolbar.add(btnRecortar);
        toolbar.addSeparator();
        toolbar.add(btnCompilar);
        toolbar.add(btnEquipe);

        // Painel para centralizar a barra de ferramentas
        JPanel toolbarPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        toolbarPanel.add(toolbar);

        // Adiciona o painel ao topo da janela
        add(toolbarPanel, BorderLayout.NORTH);

        editor = new JTextArea();
        JScrollPane scrollEditor = new JScrollPane(editor);
        scrollEditor.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollEditor.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        // Numeração de Linhas
        lineNumbers = new JTextArea("1\n");
        lineNumbers.setEditable(false);
        lineNumbers.setBackground(Color.LIGHT_GRAY);
        scrollEditor.setRowHeaderView(lineNumbers);

        // Adicionar DocumentListener ao editor para atualizar as linhas ao editar o texto
        editor.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateLineNumbers();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateLineNumbers();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateLineNumbers();
            }
        });

        // Área de Mensagens
        mensagens = new JTextArea();
        mensagens.setEditable(false);
        JScrollPane scrollMensagens = new JScrollPane(mensagens);
        scrollMensagens.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollMensagens.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        // Divisor entre Editor e Área de Mensagens
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollEditor, scrollMensagens);
        splitPane.setResizeWeight(0.7);
        add(splitPane, BorderLayout.CENTER);

        // Barra de Status
        barraStatus = new JLabel("Nenhum arquivo aberto");
        barraStatus.setBorder(new BevelBorder(BevelBorder.LOWERED));
        add(barraStatus, BorderLayout.SOUTH);

        // Ações dos Botões
        btnNovo.addActionListener(e -> novoArquivo());
        btnAbrir.addActionListener(e -> abrirArquivo());
        btnSalvar.addActionListener(e -> salvarArquivo());
        btnCopiar.addActionListener(e -> editor.copy());
        btnColar.addActionListener(e -> editor.paste());
        btnRecortar.addActionListener(e -> editor.cut());
        btnCompilar.addActionListener(e -> compilarCodigo());
        btnEquipe.addActionListener(e -> mensagens.append("Equipe de Desenvolvimento: Ranieri Marcos Tiedt, Lucas Wagner\n"));

        // Configuração de Atalhos de Teclado
        configurarAtalhos();

    }

    private void configurarAtalhos() {
        editor.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control N"), "novo");
        editor.getActionMap().put("novo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                novoArquivo();
            }
        });

        editor.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control O"), "abrir");
        editor.getActionMap().put("abrir", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirArquivo();
            }
        });

        editor.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control S"), "salvar");
        editor.getActionMap().put("salvar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvarArquivo();
            }
        });

        editor.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control C"), "copiar");
        editor.getActionMap().put("copiar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editor.copy();
            }
        });

        editor.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control V"), "colar");
        editor.getActionMap().put("colar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editor.paste();
            }
        });

        editor.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control X"), "recortar");
        editor.getActionMap().put("recortar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editor.cut();
            }
        });

        editor.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F7"), "compilar");
        editor.getActionMap().put("compilar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                compilarCodigo();
            }
        });

        editor.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F1"), "equipe");
        editor.getActionMap().put("equipe", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mensagens.append("Equipe de Desenvolvimento: Ranieri Marcos Tiedt, Lucas Wagner\n");
            }
        });
    }

    private void updateLineNumbers() {
        String[] lines = editor.getText().split("\n");
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= lines.length; i++) {
            sb.append(i).append("\n");
        }
        lineNumbers.setText(sb.toString());
    }

    private void novoArquivo() {
        editor.setText("");
        mensagens.setText("");
        barraStatus.setText("Nenhum arquivo aberto");
        arquivoAtual = null;
        updateLineNumbers(); // Atualiza as linhas ao criar um novo arquivo
    }

    private void abrirArquivo() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            arquivoAtual = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(arquivoAtual))) {
                editor.read(reader, null);
                barraStatus.setText("Aberto: " + arquivoAtual.getAbsolutePath());
                updateLineNumbers(); // Atualiza as linhas após abrir o arquivo
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao abrir o arquivo", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void salvarArquivo() {
        // Se um arquivo já estiver aberto, salvar diretamente nele
        if (arquivoAtual != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivoAtual))) {
                editor.write(writer);
                barraStatus.setText("Salvo: " + arquivoAtual.getAbsolutePath());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar o arquivo", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // Se não houver arquivo aberto, abrir o JFileChooser para salvar um novo arquivo
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new File(".txt")); // Define o nome padrão
            int result = fileChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                arquivoAtual = fileChooser.getSelectedFile();
                if (!arquivoAtual.getName().toLowerCase().endsWith(".txt")) {
                    arquivoAtual = new File(arquivoAtual.getAbsolutePath() + ".txt");
                }
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivoAtual))) {
                    editor.write(writer);
                    barraStatus.setText("Salvo: " + arquivoAtual.getAbsolutePath());
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao salvar o arquivo", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void compilarCodigo() {
        mensagens.setText(""); // Limpa a área de mensagens antes de compilar
        Lexico lexico = new Lexico();        
        lexico.setInput(editor.getText()); // Define a entrada do analisador léxico
        Sintatico sintatico = new Sintatico();
		Semantico semantico = new Semantico();

        try {
        	sintatico.parse(lexico, semantico);
        	mensagens.setText("programa compilado com sucesso");
        	
        }catch (LexicalError e) {
        	int linha = getLineFromPosition(e.getPosition());

			if (e.getMessage().equals(ScannerConstants.SCANNER_ERROR[0])) {
				mensagens.setText("Linha " + linha + " - " + editor.getText().charAt(e.getPosition()) + " " + e.getMessage());
			} else {
				mensagens.setText("Linha " + linha + " - " + e.getMessage());
			}
			
			
        }catch (SyntaticError e) {
        	int linha = getLineFromPosition(e.getPosition());
        	
        	Set<String> mensagensConhecidas = new HashSet<>(Arrays.asList(
        	    "<main> inválido",
        	    "<lista_de_instrucoes> inválido",
        	    "<lista_de_instrucoes2> inválido",
        	    "<instrucao> inválido",
        	    "<instrucao1> inválido",
        	    "<opcional> inválido",
        	    "<lista_de_comandos> inválido",
        	    "<lista_de_comandos2> inválido",
        	    "<comando> inválido",
        	    "<lista_de_identificadores> inválido",
        	    "<lista_de_identificadores2> inválido",
        	    "<lista_de_entrada> inválido",
        	    "<lista_de_entrada2> inválido",
        	    "<lista_de_expressoes> inválido",
        	    "<lista_de_expressoes_2> inválido",
        	    "<expressao> inválido",
        	    "<expressao1> inválido",
        	    "<elemento> inválido",
        	    "<relacional> inválido",
        	    "<relacional1> inválido",
        	    "<operador_relacional> inválido",
        	    "<aritmetica> inválido",
        	    "<aritmetica1> inválido",
        	    "<termo> inválido",
        	    "<termo1> inválido",
        	    "<fator> inválido"
        	));

        	if (!mensagensConhecidas.contains(e.getMessage())) {
        	    mensagens.setText("Erro na linha " + linha + " - encontrado: " + (Objects.equals(sintatico.getToken(), "$") ? "EOF" : sintatico.getToken()) + " "+ e.getMessage());
        	}else {
        	mensagens.setText("Erro na linha " + linha + " - encontrado: " + (Objects.equals(sintatico.getToken(), "$") ? "EOF" : sintatico.getToken()) + " " 
        	+ (Objects.equals(e.getMessage(),"<lista_de_entrada2> inválido" ) ? "esperado: , )" : "")
        	+ (Objects.equals(e.getMessage(),"<lista_de_entrada> inválido" ) ? "esperado: identificador constante_string" : "")
        	+ (Objects.equals(e.getMessage(),"<main> inválido" ) ? "esperado: main" : "")
        	+ (Objects.equals(e.getMessage(),"<lista_de_instrucoes> inválido" ) ? "esperado: identificador  read  write  writeln if  repeat" : "")
        	+ (Objects.equals(e.getMessage(),"<lista_de_instrucoes2> inválido" ) ? "esperado: identificador  read  write  writeln if  repeat end" : "")
        	+ (Objects.equals(e.getMessage(),"<lista_de_expressoes> inválido" ) ? "esperado: expressão" : "")
        	+ (Objects.equals(e.getMessage(),"<lista_de_expressoes2> inválido" ) ? "esperado: expressão" : "")
        	+ (Objects.equals(e.getMessage(),"<expressao1> inválido" ) ? "esperado: espressão" : "")
        	+ (Objects.equals(e.getMessage(),"<elemento> inválido" ) ? "esperado: expressão" : "")
        	+ (Objects.equals(e.getMessage(),"<relacional> inválido" ) ? "esperado: expressão" : "")
        	+ (Objects.equals(e.getMessage(),"<relacional1> inválido" ) ? "esperado: expressão" : "")
        	+ (Objects.equals(e.getMessage(),"<aritmetica> inválido" ) ? "esperado: expressão" : "")
        	+ (Objects.equals(e.getMessage(),"<aritmetica1> inválido" ) ? "esperado: expressão" : "")
        	+ (Objects.equals(e.getMessage(),"<termo> inválido" ) ? "esperado: expressão" : "")
        	+ (Objects.equals(e.getMessage(),"<termo1> inválido" ) ? "esperado: expressão" : "")
        	+ (Objects.equals(e.getMessage(),"<fator> inválido" ) ? "esperado: expressão" : "")
        	+ (Objects.equals(e.getMessage(),"<lista_de_identificadores> inválido" ) ? "esperado: identificador" : "")
        	+ (Objects.equals(e.getMessage(),"<lista_de_identificadores2> inválido" ) ? "esperado:  ,  ;  =" : "")
        	+ (Objects.equals(e.getMessage(),"<instrucao> inválido" ) ? "esperado: identificador  read  write  writeln if  repeat" : "")
        	+ (Objects.equals(e.getMessage(),"<instrucao1> inválido" ) ? "esperado: ;  =" : "")
        	+ (Objects.equals(e.getMessage(),"<opcional> inválido" ) ? "esperado: identificador constante_string" : "")
        	+ (Objects.equals(e.getMessage(),"<lista_de_comandos> inválido" ) ? "esperado: identificador  read  write  writeln if  repeat" : "")
        	+ (Objects.equals(e.getMessage(),"<lista_de_comandos2> inválido" ) ? "esperado: identificador  read  write  writeln if  repeat end elif else until while" : "")
        	+ (Objects.equals(e.getMessage(),"<comando> inválido" ) ? "esperado: identificador  read  write  writeln if  repeat" : "")
        	+ (Objects.equals(e.getMessage(),"<expressao> inválido" ) ? "esperado: expressão" : ""));
        	}
        	
		} catch (SemanticError e) {
			int linha = getLineFromPosition(e.getPosition());

			mensagens.setText("Erro na linha: " + linha + " - " + e.getMessage());
		}
	}
		

    private int getLineFromPosition(int position) {
        String text = editor.getText();
        int lineNumber = 1; 
        int currentPosition = 0; 

        while (currentPosition < position && currentPosition < text.length()) {
            if (text.charAt(currentPosition) == '\n') {
                lineNumber++;
            }
            currentPosition++;
        }
        return lineNumber;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Interface gui = new Interface();
            gui.setVisible(true);
        });
    }
}
