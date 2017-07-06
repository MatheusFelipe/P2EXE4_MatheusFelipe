package vulnerablepackage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VulnerableClass{
  private String auxiliarstring; //String para testes
  
  public String getsTest() {
    return this.auxiliarstring;
  }
  
  /**
   * Vulnerable Method.
   * 
   * @param filename input filename
   * @author Matheus
   * @throws IOException for protected files
   */
  public void vulnerableMethod(String filename) throws IOException {
    /*Caso passe nome vazio*/
    if (filename == null) {
      throw new IllegalArgumentException();
    }
    /*Sanitizacao da entrada: FILENAME devera conter apenas o nome do arquivo, 
     *sem a extensao, para nao abrir margem a caracteres especiais*/
    Pattern pattern = Pattern.compile("[^A-Za-z0-9]");
    Matcher matcher = pattern.matcher(filename);
    if (matcher.find()) {
      throw new IllegalArgumentException();
    }
    Scanner console = new Scanner(System.in);
    /*Como nao foi especificado com que tipo de arquivo irei trabalhar, padronizei .txt*/
    System.out.print("Digite a operacao desejada para realizar no arquivo .txt <R para "
        + "ler um arquivo .txt, W para escrever em um arquivo .txt> ");
    String opr = console.nextLine();
    if (opr.toUpperCase().equals("R")) {
      BufferedReader br = null;
      FileReader fr = null;
      try {
        fr = new FileReader(filename + ".txt");
        br = new BufferedReader(fr);
        auxiliarstring = "";
        String currentline;
        while ((currentline = br.readLine()) != null) {
          auxiliarstring = auxiliarstring + currentline + "\n";
        }
        System.out.print(auxiliarstring);
      } catch (IOException e) {
        e.printStackTrace();
        throw new FileNotFoundException(); //Para os testes, caso nao encontre FILENAME.txt
      } finally { /*Fechar o que foi aberto*/
        try {
          br.close();
          fr.close();
          console.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    } else if (opr.toUpperCase().equals("W")) {
      BufferedWriter buffWrite = null;
      try {
        if (filename.equals("ProtectedFile")) {
          throw new IOException();
        }
        buffWrite = new BufferedWriter(new FileWriter(filename + ".txt"));
        String linha = "";
        System.out.println("Escreva algo: ");
        linha = console.nextLine();
        buffWrite.append(linha + "\n");
        auxiliarstring = linha;
      } catch (IOException e) {
        e.printStackTrace();
        throw new IOException();
      } finally { /*Fechar o que foi aberto*/
        try {
          buffWrite.close();
          console.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    } else { /*Caso o usuario nao tenha digitado corretamente a opcao de acao do programa*/
      console.close();
      throw new IllegalArgumentException();
    }
  }
}
