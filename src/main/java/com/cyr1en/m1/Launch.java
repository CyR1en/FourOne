package com.cyr1en.m1;

import javafx.application.Application;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;

public class Launch extends Application {

  private WebView webview = new WebView();

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) {
    webview.getEngine().getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
      if (newState == Worker.State.SUCCEEDED) {
        Roll.rollOut(3, true);
      }
    });
    webview.getEngine().load(
            "https://www.youtube.com/watch?v=oHg5SJYRHA0"
    );
    webview.setPrefSize(640, 390);

    stage.setScene(new Scene(webview));
    stage.show();

    JFrame frame = new JFrame();
    frame.add(new JLabel(" Outout"), BorderLayout.NORTH);

    JTextArea ta = new JTextArea();
    TextAreaOutputStream taos = new TextAreaOutputStream(ta, 60);
    PrintStream ps = new PrintStream(taos);
    System.setOut(ps);
    System.setErr(ps);


    frame.add(new JScrollPane(ta));

    frame.pack();
    frame.setVisible(true);
    frame.setSize(800, 600);
  }

  @Override
  public void stop() throws Exception {
    webview.getEngine().load(null);
    Roll.scheduler.shutdownNow();
    super.stop();
  }

  public class TextAreaOutputStream extends OutputStream {

// *************************************************************************************************
// INSTANCE MEMBERS
// *************************************************************************************************

    private byte[] oneByte;                                                    // array for write(int val);
    private Appender appender;                                                   // most recent action

    public TextAreaOutputStream(JTextArea txtara) {
      this(txtara, 1000);
    }

    public TextAreaOutputStream(JTextArea txtara, int maxlin) {
      if (maxlin < 1) {
        throw new IllegalArgumentException("TextAreaOutputStream maximum lines must be positive (value=" + maxlin + ")");
      }
      oneByte = new byte[1];
      appender = new Appender(txtara, maxlin);
    }

    /**
     * Clear the current console text area.
     */
    public synchronized void clear() {
      if (appender != null) {
        appender.clear();
      }
    }

    public synchronized void close() {
      appender = null;
    }

    public synchronized void flush() {
    }

    public synchronized void write(int val) {
      oneByte[0] = (byte) val;
      write(oneByte, 0, 1);
    }

    public synchronized void write(byte[] ba) {
      write(ba, 0, ba.length);
    }

    public synchronized void write(byte[] ba, int str, int len) {
      if (appender != null) {
        appender.append(bytesToString(ba, str, len));
      }
    }

    private String bytesToString(byte[] ba, int str, int len) {
      try {
        return new String(ba, str, len, "UTF-8");
      } catch (UnsupportedEncodingException thr) {
        return new String(ba, str, len);
      } // all JVMs are required to support UTF-8
    }

// *************************************************************************************************
// STATIC MEMBERS
// *************************************************************************************************

    class Appender implements Runnable {
      private final JTextArea textArea;
      private final int maxLines;                                                   // maximum lines allowed in text area
      private final LinkedList<Integer> lengths;                                                    // length of lines within text area
      private final java.util.List<String> values;                                                     // values waiting to be appended

      private int curLength;                                                  // length of current line
      private boolean clear;
      private boolean queue;

      Appender(JTextArea txtara, int maxlin) {
        textArea = txtara;
        maxLines = maxlin;
        lengths = new LinkedList<>();
        values = new ArrayList<>();

        curLength = 0;
        clear = false;
        queue = true;
      }

      synchronized void append(String val) {
        values.add(val);
        if (queue) {
          queue = false;
          EventQueue.invokeLater(this);
        }
      }

      synchronized void clear() {
        clear = true;
        curLength = 0;
        lengths.clear();
        values.clear();
        if (queue) {
          queue = false;
          EventQueue.invokeLater(this);
        }
      }

      // MUST BE THE ONLY METHOD THAT TOUCHES textArea!
      public synchronized void run() {
        if (clear) {
          textArea.setText("");
        }
        for (String val : values) {
          curLength += val.length();
          if (val.endsWith(EOL1) || val.endsWith(EOL2)) {
            if (lengths.size() >= maxLines) {
              textArea.replaceRange("", 0, lengths.removeFirst());
            }
            lengths.addLast(curLength);
            curLength = 0;
          }
          textArea.append(val);
        }
        values.clear();
        clear = false;
        queue = true;
      }

      static private final String EOL1 = "\n";
      private final String EOL2 = System.getProperty("line.separator", EOL1);
    }

  }
}
