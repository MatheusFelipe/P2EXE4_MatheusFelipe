package test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import vulnerablepackage.VulnerableClass;

public class VulnerableClassTest {
	
	/*Procedimento padrao de Before e After para não precisar que usuario use o console para os testes*/
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
	
	@Before
	public void setUpStreams() {
	    System.setOut(new PrintStream(outContent));
	    System.setErr(new PrintStream(errContent));
	}
	
	@After
	public void cleanUpStreams() {
	    System.setOut(null);
	    System.setErr(null);
	}
	
	@Test(expected = IllegalArgumentException.class)  
	public void nullFILENAMEtest_expectsIllegalArgumentException() throws IOException {
		String nullFILENAME = null;
		VulnerableClass v = new VulnerableClass();
		v.vulnerableMethod(nullFILENAME);		
	}
	
	@Test(expected = IllegalArgumentException.class)  
	public void FILENAMEwithPATHtoSystemFiles_expectsIllegalArgumentException() throws IOException {
		String PATHFILENAME = "C:\\Windows\\System32";
		VulnerableClass v = new VulnerableClass();
		v.vulnerableMethod(PATHFILENAME);
	}
	
	@Test(expected = IllegalArgumentException.class)  
	public void FILENAMEwithEscapeSequences_expectsIllegalArgumentException() throws IOException {
		String FILENAMEwithEscapeSequences = "\b\t\n\r\f\\\"";
		VulnerableClass v = new VulnerableClass();
		v.vulnerableMethod(FILENAMEwithEscapeSequences);
	}
	
	@Test(expected = IllegalArgumentException.class)  
	public void FILENAMEwithProhibitedExtension_expectsIllegalArgumentException() throws IOException {
		String anotherExtensionFILENAME = "executavel.jar";
		VulnerableClass v = new VulnerableClass();
		v.vulnerableMethod(anotherExtensionFILENAME);
	}
	
	@Test(expected = IllegalArgumentException.class)  
	public void TypoFILENAME_expectsIllegalArgumentException() throws IOException {
		String typoFILENAME = "filename?";
		VulnerableClass v = new VulnerableClass();
		v.vulnerableMethod(typoFILENAME);
	}
	
	@Test(expected = IllegalArgumentException.class) 
	public void CorrectFILENAME_UserTypo_expectsIllegalArgumentException() throws IOException {
		String userTypoFILENAME = "UserTypo";
		VulnerableClass v = new VulnerableClass();
		String input = "Typo\n";
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);
		v.vulnerableMethod(userTypoFILENAME);
	}
	
	@Test  
	public void CorrectFILENAME_UserTypesR() throws IOException {
		String FILENAME = "ReadTest";
		VulnerableClass v = new VulnerableClass();
		String input = "R\n";
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);
		v.vulnerableMethod(FILENAME);
		assertEquals("Digite a operacao desejada para realizar no arquivo .txt <R para ler um arquivo .txt, "
		    		+ "W para escrever em um arquivo .txt> " + v.getsTest() , outContent.toString());
	}
	
	@Test (expected = FileNotFoundException.class)
	public void NonexistentFILENAME_UserTypesR_expectsFileNotFoundException() throws IOException {
		String inexistentFILENAME = "InexistentFile";
		VulnerableClass v = new VulnerableClass();
		String input = "R\n";
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);
		v.vulnerableMethod(inexistentFILENAME);
	}
	
	@Test 
	public void CorrectFILENAME_UserTypesW() throws IOException {
		String FILENAME = "WriteTest";
		VulnerableClass v = new VulnerableClass();
		String input = "W" + System.getProperty("line.separator") + "Sample Text"+ System.getProperty("line.separator");
		System.setIn(new ByteArrayInputStream(input.getBytes()));
		v.vulnerableMethod(FILENAME);
	}
	@Test (expected = IOException.class)
	public void CorrectFILENAME_UserTypesW_UnreachableFile() throws IOException {
		String FILENAME = "ProtectedFile";
		VulnerableClass v = new VulnerableClass();
		String input = "W" + System.getProperty("line.separator") + "Sample Text"+ System.getProperty("line.separator");
		System.setIn(new ByteArrayInputStream(input.getBytes()));
		v.vulnerableMethod(FILENAME);
	}
}

