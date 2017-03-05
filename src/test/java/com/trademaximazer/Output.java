package com.trademaximazer;

import java.io.IOException;
import java.io.OutputStream;

public class Output {
	
	private StringBuffer out = new StringBuffer();
	private OutputStream outputStream;
	
	public Output(OutputStream outputStream) {
		this.outputStream = outputStream; 
	}

	public void print(String s) {
		out.append(s);
	}

	public void println(String s) {
		out.append(s);
		out.append("\n");
	}
	
	public String getOutputString() {
		return out.toString();
	}

	public void println() {
		out.append("\n");
	}
	
	public void output() throws IOException {
		outputStream.write(out.toString().getBytes());
	}
}
