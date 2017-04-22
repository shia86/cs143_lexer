/*
 *  The scanner definition for COOL.
 */
import java_cup.runtime.Symbol;


class CoolLexer implements java_cup.runtime.Scanner {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 128;
	private final int YY_EOF = 129;

/*  Stuff enclosed in %{ %} is copied verbatim to the lexer class
 *  definition, all the extra variables/functions you want to use in the
 *  lexer actions should go here.  Don't remove or modify anything that
 *  was there initially.  */
    // Max size of string constants
    static int MAX_STR_CONST = 1025;
    // For assembling string constants
    StringBuffer string_buf = new StringBuffer();
    private int curr_lineno = 1;
    private int cmt_depth = 0;
    int get_curr_lineno() {
  return curr_lineno;
    }
    private AbstractSymbol filename;
    void set_filename(String fname) {
  filename = AbstractTable.stringtable.addString(fname);
    }
    AbstractSymbol curr_filename() {
  return filename;
    }
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private boolean yy_at_bol;
	private int yy_lexical_state;

	CoolLexer (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	CoolLexer (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private CoolLexer () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yy_at_bol = true;
		yy_lexical_state = YYINITIAL;

/*  Stuff enclosed in %init{ %init} is copied verbatim to the lexer
 *  class constructor, all the extra initialization you want to do should
 *  go here.  Don't remove or modify anything that was there initially. */
    // empty for now
	}

	private boolean yy_eof_done = false;
	private final int STRING = 2;
	private final int YYINITIAL = 0;
	private final int COMMENT = 1;
	private final int yy_state_dtrans[] = {
		0,
		52,
		83
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private int yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_end () {
		if (yy_buffer_end > yy_buffer_start &&
		    '\n' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
		if (yy_buffer_end > yy_buffer_start &&
		    '\r' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
	}
	private boolean yy_last_was_cr=false;
	private void yy_mark_start () {
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
		yy_at_bol = (yy_buffer_end > yy_buffer_start) &&
		            ('\r' == yy_buffer[yy_buffer_end-1] ||
		             '\n' == yy_buffer[yy_buffer_end-1] ||
		             2028/*LS*/ == yy_buffer[yy_buffer_end-1] ||
		             2029/*PS*/ == yy_buffer[yy_buffer_end-1]);
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
	private int[][] unpackFromString(int size1, int size2, String st) {
		int colonIndex = -1;
		String lengthString;
		int sequenceLength = 0;
		int sequenceInteger = 0;

		int commaIndex;
		String workString;

		int res[][] = new int[size1][size2];
		for (int i= 0; i < size1; i++) {
			for (int j= 0; j < size2; j++) {
				if (sequenceLength != 0) {
					res[i][j] = sequenceInteger;
					sequenceLength--;
					continue;
				}
				commaIndex = st.indexOf(',');
				workString = (commaIndex==-1) ? st :
					st.substring(0, commaIndex);
				st = st.substring(commaIndex+1);
				colonIndex = workString.indexOf(':');
				if (colonIndex == -1) {
					res[i][j]=Integer.parseInt(workString);
					continue;
				}
				lengthString =
					workString.substring(colonIndex+1);
				sequenceLength=Integer.parseInt(lengthString);
				workString=workString.substring(0,colonIndex);
				sequenceInteger=Integer.parseInt(workString);
				res[i][j] = sequenceInteger;
				sequenceLength--;
			}
		}
		return res;
	}
	private int yy_acpt[] = {
		/* 0 */ YY_NOT_ACCEPT,
		/* 1 */ YY_NO_ANCHOR,
		/* 2 */ YY_NO_ANCHOR,
		/* 3 */ YY_NO_ANCHOR,
		/* 4 */ YY_NO_ANCHOR,
		/* 5 */ YY_NO_ANCHOR,
		/* 6 */ YY_NO_ANCHOR,
		/* 7 */ YY_NO_ANCHOR,
		/* 8 */ YY_NO_ANCHOR,
		/* 9 */ YY_NO_ANCHOR,
		/* 10 */ YY_NO_ANCHOR,
		/* 11 */ YY_NO_ANCHOR,
		/* 12 */ YY_NO_ANCHOR,
		/* 13 */ YY_NO_ANCHOR,
		/* 14 */ YY_NO_ANCHOR,
		/* 15 */ YY_NO_ANCHOR,
		/* 16 */ YY_NO_ANCHOR,
		/* 17 */ YY_NO_ANCHOR,
		/* 18 */ YY_NO_ANCHOR,
		/* 19 */ YY_NO_ANCHOR,
		/* 20 */ YY_NO_ANCHOR,
		/* 21 */ YY_NO_ANCHOR,
		/* 22 */ YY_NO_ANCHOR,
		/* 23 */ YY_NO_ANCHOR,
		/* 24 */ YY_NO_ANCHOR,
		/* 25 */ YY_NO_ANCHOR,
		/* 26 */ YY_NO_ANCHOR,
		/* 27 */ YY_NO_ANCHOR,
		/* 28 */ YY_NO_ANCHOR,
		/* 29 */ YY_NO_ANCHOR,
		/* 30 */ YY_NO_ANCHOR,
		/* 31 */ YY_NO_ANCHOR,
		/* 32 */ YY_NO_ANCHOR,
		/* 33 */ YY_NO_ANCHOR,
		/* 34 */ YY_NO_ANCHOR,
		/* 35 */ YY_NO_ANCHOR,
		/* 36 */ YY_NO_ANCHOR,
		/* 37 */ YY_NO_ANCHOR,
		/* 38 */ YY_NO_ANCHOR,
		/* 39 */ YY_NO_ANCHOR,
		/* 40 */ YY_NO_ANCHOR,
		/* 41 */ YY_NO_ANCHOR,
		/* 42 */ YY_NO_ANCHOR,
		/* 43 */ YY_NO_ANCHOR,
		/* 44 */ YY_NO_ANCHOR,
		/* 45 */ YY_NO_ANCHOR,
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NO_ANCHOR,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NO_ANCHOR,
		/* 54 */ YY_NO_ANCHOR,
		/* 55 */ YY_NOT_ACCEPT,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NO_ANCHOR,
		/* 59 */ YY_NO_ANCHOR,
		/* 60 */ YY_NO_ANCHOR,
		/* 61 */ YY_NO_ANCHOR,
		/* 62 */ YY_NO_ANCHOR,
		/* 63 */ YY_NO_ANCHOR,
		/* 64 */ YY_NO_ANCHOR,
		/* 65 */ YY_NOT_ACCEPT,
		/* 66 */ YY_NO_ANCHOR,
		/* 67 */ YY_NO_ANCHOR,
		/* 68 */ YY_NO_ANCHOR,
		/* 69 */ YY_NOT_ACCEPT,
		/* 70 */ YY_NO_ANCHOR,
		/* 71 */ YY_NO_ANCHOR,
		/* 72 */ YY_NOT_ACCEPT,
		/* 73 */ YY_NO_ANCHOR,
		/* 74 */ YY_NO_ANCHOR,
		/* 75 */ YY_NOT_ACCEPT,
		/* 76 */ YY_NO_ANCHOR,
		/* 77 */ YY_NOT_ACCEPT,
		/* 78 */ YY_NO_ANCHOR,
		/* 79 */ YY_NOT_ACCEPT,
		/* 80 */ YY_NO_ANCHOR,
		/* 81 */ YY_NOT_ACCEPT,
		/* 82 */ YY_NO_ANCHOR,
		/* 83 */ YY_NOT_ACCEPT,
		/* 84 */ YY_NO_ANCHOR,
		/* 85 */ YY_NO_ANCHOR,
		/* 86 */ YY_NO_ANCHOR,
		/* 87 */ YY_NO_ANCHOR,
		/* 88 */ YY_NO_ANCHOR,
		/* 89 */ YY_NO_ANCHOR,
		/* 90 */ YY_NO_ANCHOR,
		/* 91 */ YY_NO_ANCHOR,
		/* 92 */ YY_NO_ANCHOR,
		/* 93 */ YY_NO_ANCHOR,
		/* 94 */ YY_NO_ANCHOR,
		/* 95 */ YY_NO_ANCHOR,
		/* 96 */ YY_NO_ANCHOR,
		/* 97 */ YY_NO_ANCHOR,
		/* 98 */ YY_NO_ANCHOR,
		/* 99 */ YY_NO_ANCHOR,
		/* 100 */ YY_NO_ANCHOR,
		/* 101 */ YY_NO_ANCHOR,
		/* 102 */ YY_NO_ANCHOR,
		/* 103 */ YY_NO_ANCHOR,
		/* 104 */ YY_NO_ANCHOR,
		/* 105 */ YY_NO_ANCHOR,
		/* 106 */ YY_NO_ANCHOR,
		/* 107 */ YY_NO_ANCHOR,
		/* 108 */ YY_NO_ANCHOR,
		/* 109 */ YY_NO_ANCHOR,
		/* 110 */ YY_NO_ANCHOR,
		/* 111 */ YY_NO_ANCHOR,
		/* 112 */ YY_NO_ANCHOR,
		/* 113 */ YY_NO_ANCHOR,
		/* 114 */ YY_NO_ANCHOR,
		/* 115 */ YY_NO_ANCHOR,
		/* 116 */ YY_NO_ANCHOR,
		/* 117 */ YY_NO_ANCHOR,
		/* 118 */ YY_NO_ANCHOR,
		/* 119 */ YY_NO_ANCHOR,
		/* 120 */ YY_NO_ANCHOR,
		/* 121 */ YY_NO_ANCHOR,
		/* 122 */ YY_NO_ANCHOR,
		/* 123 */ YY_NO_ANCHOR,
		/* 124 */ YY_NO_ANCHOR,
		/* 125 */ YY_NO_ANCHOR,
		/* 126 */ YY_NO_ANCHOR,
		/* 127 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"4,54:8,55,8,55:2,9,54:18,55,54,52,54:5,7,6,5,32,30,3,33,31,34:10,38,28,29,1" +
",2,54,47,21,48,27,48,19,48:2,45,48:3,22,48,46,48:3,17,23,44,49,48:5,54,53,5" +
"4:2,50,54,41,51,42,37,13,20,51,12,10,51:2,26,51,11,25,24,51,14,16,15,18,36," +
"35,51:3,40,54,43,39,54,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,128,
"0,1,2,1,3,4,1,5,1,6,7,8,1,9,1:4,10,1:5,11,1,12,1:2,13,14:3,1:2,15,1,14:3,1," +
"14:6,8:2,14:3,16,1:2,17,18,19,20,21,8:2,14:2,22,23,24,25,26,27,28,29,15,30," +
"31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55," +
"56,57,58,59,60,61,62,63,64,65,14,66,67,68,69,70,71,72,73,74,75,76,77,78,79," +
"80,81,82,83")[0];

	private int yy_nxt[][] = unpackFromString(84,56,
"1,2,3,4,3,5,6,7,8,9,10,57,109,114,109,117,109,11,109,124,67,11:3,119,70,120" +
",125,12,13,14,15,16,17,18,121,109:2,19,20,21,109,122,22,126,11:2,23,11:2,3," +
"109,24,3:2,9,-1:58,25,-1:56,26,-1:58,27,-1:54,28,-1:59,9,-1:45,9,-1:10,109," +
"29,109:4,123,109:3,30,109:7,-1:6,109:4,-1:3,109:2,-1,109:3,-1,109:4,-1:14,1" +
"1:18,-1:6,11:4,-1:3,11:2,-1,11:3,-1,11:4,-1:5,33,-1,34,-1:86,18,-1:22,24:3," +
"55,24:3,35,24:43,36,65,24:2,-1,26:7,-1,26:47,-1:10,109:2,90,109:15,-1:6,109" +
":4,-1:3,109:2,-1,109:3,-1,109:4,-1:14,109:18,-1:6,109:4,-1:3,109:2,-1,109:3" +
",-1,109:4,-1:5,72:3,-1,72:3,35,72:43,-1,75,72:2,1,64:4,56,64,66,64:48,-1,55" +
":7,40,55:43,40,69,55:2,-1,64:5,53,64:49,-1:10,109:3,73,109:11,76,109:2,-1:6" +
",109:4,-1:3,109:2,-1,109:3,-1,109:4,-1:14,11:3,60,11:5,60,11:8,-1:6,11:4,-1" +
":3,11:2,-1,11:3,-1,11:4,-1:5,59:3,-1,59:3,35,59:43,36,77,59:2,-1,64:4,79,64" +
",81,64:48,-1,24:3,55,24:3,59:2,24:46,-1,64:4,54,64:50,-1:10,31,109:10,85,10" +
"9:6,-1:6,109:4,-1:3,85,109,-1,109:3,-1,109:4,-1:14,11:3,47,11:5,47,11:8,-1:" +
"6,11:4,-1:3,11:2,-1,11:3,-1,11:4,-1:5,55:7,-1:2,55:46,-1:10,109:10,32,109:7" +
",-1:6,109:4,-1:3,109:2,-1,109:3,-1,109:4,-1:14,11,61,11:16,-1:6,11:4,-1:3,1" +
"1:2,-1,11:2,61,-1,11:4,-1:14,109:18,-1:6,109,37,109:2,-1:3,109:2,-1,109:3,-" +
"1,109:4,-1:14,11:6,48,11:6,48,11:4,-1:6,11:4,-1:3,11:2,-1,11:3,-1,11:4,-1:5" +
",72:3,-1,72:51,-1:10,109:5,38,109:12,-1:6,109:4,-1:3,109:2,-1,109:3,-1,109:" +
"4,-1:5,59:3,-1,59:51,-1:10,109:18,-1:6,109:4,-1:3,91,109,-1,109:3,-1,109:4," +
"-1:5,64:5,-1,64:49,-1:10,109:6,92,109:6,92,109:4,-1:6,109:4,-1:3,109:2,-1,1" +
"09:3,-1,109:4,-1:5,64:4,-1,64:50,-1:10,109:3,93,109:5,93,109:8,-1:6,109:4,-" +
"1:3,109:2,-1,109:3,-1,109:4,-1:4,1,3:7,-1:2,3:46,-1:10,109:8,94,109:9,-1:6," +
"109:4,-1:3,109:2,-1,109:3,-1,109,94,109:2,-1:14,109:12,95,109:3,95,109,-1:6" +
",109:4,-1:3,109:2,-1,109:3,-1,109:4,-1:14,109:15,96,109:2,-1:6,109:4,-1:3,1" +
"09:2,-1,109:3,-1,109:4,-1:14,109:5,39,109:12,-1:6,109:4,-1:3,109:2,-1,109:3" +
",-1,109:4,-1:14,98,109:17,-1:6,109:4,-1:3,109:2,-1,109:3,-1,109:4,-1:14,109" +
":11,100,109:6,-1:6,109:4,-1:3,100,109,-1,109:3,-1,109:4,-1:14,109:3,101,109" +
":14,-1:6,109:4,-1:3,109:2,-1,109:3,-1,109:4,-1:14,109:18,-1:6,109:4,-1:3,10" +
"9,41,-1,109:3,-1,109:4,-1:14,109:3,42,109:5,42,109:8,-1:6,109:4,-1:3,109:2," +
"-1,109:3,-1,109:4,-1:14,109,43,109:16,-1:6,109:4,-1:3,109:2,-1,109:2,43,-1," +
"109:4,-1:14,109:3,44,109:5,44,109:8,-1:6,109:4,-1:3,109:2,-1,109:3,-1,109:4" +
",-1:14,109:6,94,109:6,94,109:4,-1:6,109:4,-1:3,109:2,-1,109:3,-1,109:4,-1:1" +
"4,109:16,45,109,-1:6,109:4,-1:3,109:2,-1,109:3,-1,109:4,-1:14,109:14,46,109" +
":3,-1:6,109:4,-1:3,109:2,-1,109:3,-1,109:4,-1:14,109:16,103,109,-1:6,109:4," +
"-1:3,109:2,-1,109:3,-1,109:4,-1:14,109:3,62,109:5,62,109:8,-1:6,109:4,-1:3," +
"109:2,-1,109:3,-1,109:4,-1:14,109:6,104,109:6,104,109:4,-1:6,109:4,-1:3,109" +
":2,-1,109:3,-1,109:4,-1:14,109:4,112,109:13,-1:6,109:4,-1:3,109:2,-1,109:3," +
"-1,109:4,-1:14,105,109:17,-1:6,109:4,-1:3,109:2,-1,109:3,-1,109:4,-1:14,109" +
":3,49,109:14,-1:6,109:4,-1:3,109:2,-1,109:3,-1,109:4,-1:14,109:6,63,109:6,6" +
"3,109:4,-1:6,109:4,-1:3,109:2,-1,109:3,-1,109:4,-1:14,109:18,-1:6,109:3,50," +
"-1:3,109:2,-1,109:3,-1,109:4,-1:14,109:5,107,109:12,-1:6,109:4,-1:3,109:2,-" +
"1,109:3,-1,109:4,-1:14,109:6,51,109:11,-1:6,109:4,-1:3,109:2,-1,109:3,-1,10" +
"9:4,-1:14,11:6,58,11:6,58,11:4,-1:6,11:4,-1:3,11:2,-1,11:3,-1,11:4,-1:14,10" +
"9:6,99,109:6,99,109:4,-1:6,109:4,-1:3,109:2,-1,109:3,-1,109:4,-1:14,109:15," +
"97,109:2,-1:6,109:4,-1:3,109:2,-1,109:3,-1,109:4,-1:14,106,109:17,-1:6,109:" +
"4,-1:3,109:2,-1,109:3,-1,109:4,-1:14,11:6,68,11:6,68,11:4,-1:6,11:4,-1:3,11" +
":2,-1,11:3,-1,11:4,-1:14,109:6,78,109:5,80,109:3,80,109,-1:6,109:4,-1:3,109" +
":2,-1,109:3,-1,109:4,-1:14,109:15,102,109:2,-1:6,109:4,-1:3,109:2,-1,109:3," +
"-1,109:4,-1:14,11:3,71,11:5,71,11:8,-1:6,11:4,-1:3,11:2,-1,11:3,-1,11:4,-1:" +
"14,109:2,82,109,84,109:2,84,109:10,-1:6,109:4,-1:3,109:2,-1,109,82,109,-1,1" +
"09:4,-1:14,11:6,74,11:6,74,11:4,-1:6,11:4,-1:3,11:2,-1,11:3,-1,11:4,-1:14,1" +
"09:15,86,109:2,-1:6,109:4,-1:3,109:2,-1,109:3,-1,109:4,-1:14,109:3,87,109:1" +
"1,111,109:2,-1:6,109:4,-1:3,109:2,-1,109:3,-1,109:4,-1:14,109:2,88,109:15,-" +
"1:6,109:4,-1:3,109:2,-1,109:3,-1,109:4,-1:14,109:11,110,89,109:3,89,109,-1:" +
"6,109:4,-1:3,110,109,-1,109:3,-1,109:4,-1:14,109:18,-1:6,109:2,115,109,-1:3" +
",109:2,-1,109:3,-1,109:4,-1:14,11:12,108,11:3,108,11,-1:6,11:4,-1:3,11:2,-1" +
",11:3,-1,11:4,-1:14,11:11,113,127,11:3,127,11,-1:6,11:4,-1:3,113,11,-1,11:3" +
",-1,11:4,-1:14,11:2,116,11:15,-1:6,11:4,-1:3,11:2,-1,11,116,11,-1,11:4,-1:1" +
"4,11:11,118,11:6,-1:6,11:4,-1:3,118,11,-1,11:3,-1,11:4,-1:4");

	public java_cup.runtime.Symbol next_token ()
		throws java.io.IOException {
		int yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			if (yy_initial && yy_at_bol) yy_lookahead = YY_BOL;
			else yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			if (YY_EOF == yy_lookahead && true == yy_initial) {

/*  Stuff enclosed in %eofval{ %eofval} specifies java code that is
 *  executed when end-of-file is reached.  If you use multiple lexical
 *  states and want to do something special if an EOF is encountered in
 *  one of those states, place your code in the switch statement.
 *  Ultimately, you should return the EOF symbol, or your lexer won't
 *  work.  */
    switch(yy_lexical_state) {
    case YYINITIAL:
      /* nothing special to do in the initial state */
      break;
      /* If necessary, add code for other states here, e.g:*/
    case COMMENT:
      yybegin(YYINITIAL); 
      if (cmt_depth > 0 )
        return new Symbol(TokenConstants.ERROR, "EOF in comment");
    }
    return new Symbol(TokenConstants.EOF);
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_move_end();
					}
					yy_to_mark();
					switch (yy_last_accept_state) {
					case 1:
						
					case -2:
						break;
					case 2:
						{  return new Symbol(TokenConstants.EQ); }
					case -3:
						break;
					case 3:
						{ /* This rule should be the very last
                      in your lexical specification and
                      will match match everything not
                      matched by other lexical rules. */
                      return new Symbol(TokenConstants.ERROR, yytext()); }
					case -4:
						break;
					case 4:
						{ return new Symbol(TokenConstants.MINUS);}
					case -5:
						break;
					case 5:
						{ return new Symbol(TokenConstants.MULT);}
					case -6:
						break;
					case 6:
						{ return new Symbol(TokenConstants.RPAREN);}
					case -7:
						break;
					case 7:
						{ return new Symbol(TokenConstants.LPAREN);}
					case -8:
						break;
					case 8:
						{ curr_lineno++; }
					case -9:
						break;
					case 9:
						{ /* white space */ }
					case -10:
						break;
					case 10:
						{return new Symbol(TokenConstants.OBJECTID,new IdSymbol(yytext(), yytext().length(), 0)); }
					case -11:
						break;
					case 11:
						{return new Symbol(TokenConstants.TYPEID, new IdSymbol(yytext(), yytext().length(), 0));}
					case -12:
						break;
					case 12:
						{ return new Symbol(TokenConstants.SEMI);}
					case -13:
						break;
					case 13:
						{ return new Symbol(TokenConstants.LT);}
					case -14:
						break;
					case 14:
						{ return new Symbol(TokenConstants.COMMA);}
					case -15:
						break;
					case 15:
						{  return new Symbol(TokenConstants.DIV); }
					case -16:
						break;
					case 16:
						{  return new Symbol(TokenConstants.PLUS); }
					case -17:
						break;
					case 17:
						{  return new Symbol(TokenConstants.DOT); }
					case -18:
						break;
					case 18:
						{  return new Symbol(TokenConstants.INT_CONST, new IntSymbol(yytext(), yytext().length(), 0)); }
					case -19:
						break;
					case 19:
						{  return new Symbol(TokenConstants.COLON); }
					case -20:
						break;
					case 20:
						{  return new Symbol(TokenConstants.NEG); }
					case -21:
						break;
					case 21:
						{  return new Symbol(TokenConstants.LBRACE); }
					case -22:
						break;
					case 22:
						{  return new Symbol(TokenConstants.RBRACE); }
					case -23:
						break;
					case 23:
						{  return new Symbol(TokenConstants.AT); }
					case -24:
						break;
					case 24:
						{ return new Symbol(TokenConstants.ERROR, "EOF in string constant");}
					case -25:
						break;
					case 25:
						{ /* Sample lexical rule for "=>" arrow.
                                     Further lexical rules should be defined
                                     here, after the last %% separator */
                      return new Symbol(TokenConstants.DARROW); }
					case -26:
						break;
					case 26:
						{ /* comment */; }
					case -27:
						break;
					case 27:
						{ return new Symbol(TokenConstants.ERROR, yytext());}
					case -28:
						break;
					case 28:
						{ cmt_depth++;
                      yybegin(COMMENT);
                    }
					case -29:
						break;
					case 29:
						{ return new Symbol(TokenConstants.IN);}
					case -30:
						break;
					case 30:
						{  return new Symbol(TokenConstants.IF); }
					case -31:
						break;
					case 31:
						{  return new Symbol(TokenConstants.FI); }
					case -32:
						break;
					case 32:
						{  return new Symbol(TokenConstants.OF); }
					case -33:
						break;
					case 33:
						{  return new Symbol(TokenConstants.LE); }
					case -34:
						break;
					case 34:
						{  return new Symbol(TokenConstants.ASSIGN); }
					case -35:
						break;
					case 35:
						{ return new Symbol(TokenConstants.ERROR, "Unterminated string constant");}
					case -36:
						break;
					case 36:
						{ yybegin(YYINITIAL);String text = yytext(); 
StringBuilder value = new StringBuilder();
for(int i = 1; i<text.length()-1;i++){
  char c = text.charAt(i);
  if (c == '\\') {
    i++;
    char cNext = text.charAt(i);  
    switch (cNext) {
      case '\\': c = '\\'; break;
      case '"': c = '\"'; break;
      case 'n': c = '\n'; break;
      case 't': c = '\t'; break;
      case 'b': c = '\b'; break;
      case 'f': c = '\f'; break;
      default: c = cNext; break;
    }
  }
  value.append(c);  
}
text = value.toString();
if (text.length() >= MAX_STR_CONST)
  return new Symbol(TokenConstants.ERROR, "String constant too long");
return new Symbol(TokenConstants.STR_CONST, new StringSymbol(text, text.length(), 0)); }
					case -37:
						break;
					case 37:
						{  return new Symbol(TokenConstants.NEW); }
					case -38:
						break;
					case 38:
						{ return new Symbol(TokenConstants.NOT);}
					case -39:
						break;
					case 39:
						{  return new Symbol(TokenConstants.LET); }
					case -40:
						break;
					case 40:
						{ return new Symbol(TokenConstants.ERROR, "String contains null character");}
					case -41:
						break;
					case 41:
						{  return new Symbol(TokenConstants.ESAC); }
					case -42:
						break;
					case 42:
						{  return new Symbol(TokenConstants.ELSE); }
					case -43:
						break;
					case 43:
						{  return new Symbol(TokenConstants.THEN); }
					case -44:
						break;
					case 44:
						{ return new Symbol(TokenConstants.BOOL_CONST, new Boolean(yytext().toLowerCase()));}
					case -45:
						break;
					case 45:
						{ return new Symbol(TokenConstants.POOL);}
					case -46:
						break;
					case 46:
						{  return new Symbol(TokenConstants.LOOP); }
					case -47:
						break;
					case 47:
						{ return new Symbol(TokenConstants.CASE);}
					case -48:
						break;
					case 48:
						{  return new Symbol(TokenConstants.CLASS); }
					case -49:
						break;
					case 49:
						{  return new Symbol(TokenConstants.WHILE); }
					case -50:
						break;
					case 50:
						{  return new Symbol(TokenConstants.ISVOID); }
					case -51:
						break;
					case 51:
						{ return new Symbol(TokenConstants.INHERITS);}
					case -52:
						break;
					case 52:
						{ /*comment*/ }
					case -53:
						break;
					case 53:
						{ cmt_depth--; 
                  if(cmt_depth==0) 
                    yybegin(YYINITIAL); 
               }
					case -54:
						break;
					case 54:
						{ cmt_depth++; }
					case -55:
						break;
					case 56:
						{ /* This rule should be the very last
                      in your lexical specification and
                      will match match everything not
                      matched by other lexical rules. */
                      return new Symbol(TokenConstants.ERROR, yytext()); }
					case -56:
						break;
					case 57:
						{return new Symbol(TokenConstants.OBJECTID,new IdSymbol(yytext(), yytext().length(), 0)); }
					case -57:
						break;
					case 58:
						{return new Symbol(TokenConstants.TYPEID, new IdSymbol(yytext(), yytext().length(), 0));}
					case -58:
						break;
					case 59:
						{ return new Symbol(TokenConstants.ERROR, "EOF in string constant");}
					case -59:
						break;
					case 60:
						{  return new Symbol(TokenConstants.ELSE); }
					case -60:
						break;
					case 61:
						{  return new Symbol(TokenConstants.THEN); }
					case -61:
						break;
					case 62:
						{ return new Symbol(TokenConstants.CASE);}
					case -62:
						break;
					case 63:
						{  return new Symbol(TokenConstants.CLASS); }
					case -63:
						break;
					case 64:
						{ /*comment*/ }
					case -64:
						break;
					case 66:
						{ /* This rule should be the very last
                      in your lexical specification and
                      will match match everything not
                      matched by other lexical rules. */
                      return new Symbol(TokenConstants.ERROR, yytext()); }
					case -65:
						break;
					case 67:
						{return new Symbol(TokenConstants.OBJECTID,new IdSymbol(yytext(), yytext().length(), 0)); }
					case -66:
						break;
					case 68:
						{return new Symbol(TokenConstants.TYPEID, new IdSymbol(yytext(), yytext().length(), 0));}
					case -67:
						break;
					case 70:
						{return new Symbol(TokenConstants.OBJECTID,new IdSymbol(yytext(), yytext().length(), 0)); }
					case -68:
						break;
					case 71:
						{return new Symbol(TokenConstants.TYPEID, new IdSymbol(yytext(), yytext().length(), 0));}
					case -69:
						break;
					case 73:
						{return new Symbol(TokenConstants.OBJECTID,new IdSymbol(yytext(), yytext().length(), 0)); }
					case -70:
						break;
					case 74:
						{return new Symbol(TokenConstants.TYPEID, new IdSymbol(yytext(), yytext().length(), 0));}
					case -71:
						break;
					case 76:
						{return new Symbol(TokenConstants.OBJECTID,new IdSymbol(yytext(), yytext().length(), 0)); }
					case -72:
						break;
					case 78:
						{return new Symbol(TokenConstants.OBJECTID,new IdSymbol(yytext(), yytext().length(), 0)); }
					case -73:
						break;
					case 80:
						{return new Symbol(TokenConstants.OBJECTID,new IdSymbol(yytext(), yytext().length(), 0)); }
					case -74:
						break;
					case 82:
						{return new Symbol(TokenConstants.OBJECTID,new IdSymbol(yytext(), yytext().length(), 0)); }
					case -75:
						break;
					case 84:
						{return new Symbol(TokenConstants.OBJECTID,new IdSymbol(yytext(), yytext().length(), 0)); }
					case -76:
						break;
					case 85:
						{return new Symbol(TokenConstants.OBJECTID,new IdSymbol(yytext(), yytext().length(), 0)); }
					case -77:
						break;
					case 86:
						{return new Symbol(TokenConstants.OBJECTID,new IdSymbol(yytext(), yytext().length(), 0)); }
					case -78:
						break;
					case 87:
						{return new Symbol(TokenConstants.OBJECTID,new IdSymbol(yytext(), yytext().length(), 0)); }
					case -79:
						break;
					case 88:
						{return new Symbol(TokenConstants.OBJECTID,new IdSymbol(yytext(), yytext().length(), 0)); }
					case -80:
						break;
					case 89:
						{return new Symbol(TokenConstants.OBJECTID,new IdSymbol(yytext(), yytext().length(), 0)); }
					case -81:
						break;
					case 90:
						{return new Symbol(TokenConstants.OBJECTID,new IdSymbol(yytext(), yytext().length(), 0)); }
					case -82:
						break;
					case 91:
						{return new Symbol(TokenConstants.OBJECTID,new IdSymbol(yytext(), yytext().length(), 0)); }
					case -83:
						break;
					case 92:
						{return new Symbol(TokenConstants.OBJECTID,new IdSymbol(yytext(), yytext().length(), 0)); }
					case -84:
						break;
					case 93:
						{return new Symbol(TokenConstants.OBJECTID,new IdSymbol(yytext(), yytext().length(), 0)); }
					case -85:
						break;
					case 94:
						{return new Symbol(TokenConstants.OBJECTID,new IdSymbol(yytext(), yytext().length(), 0)); }
					case -86:
						break;
					case 95:
						{return new Symbol(TokenConstants.OBJECTID,new IdSymbol(yytext(), yytext().length(), 0)); }
					case -87:
						break;
					case 96:
						{return new Symbol(TokenConstants.OBJECTID,new IdSymbol(yytext(), yytext().length(), 0)); }
					case -88:
						break;
					case 97:
						{return new Symbol(TokenConstants.OBJECTID,new IdSymbol(yytext(), yytext().length(), 0)); }
					case -89:
						break;
					case 98:
						{return new Symbol(TokenConstants.OBJECTID,new IdSymbol(yytext(), yytext().length(), 0)); }
					case -90:
						break;
					case 99:
						{return new Symbol(TokenConstants.OBJECTID,new IdSymbol(yytext(), yytext().length(), 0)); }
					case -91:
						break;
					case 100:
						{return new Symbol(TokenConstants.OBJECTID,new IdSymbol(yytext(), yytext().length(), 0)); }
					case -92:
						break;
					case 101:
						{return new Symbol(TokenConstants.OBJECTID,new IdSymbol(yytext(), yytext().length(), 0)); }
					case -93:
						break;
					case 102:
						{return new Symbol(TokenConstants.OBJECTID,new IdSymbol(yytext(), yytext().length(), 0)); }
					case -94:
						break;
					case 103:
						{return new Symbol(TokenConstants.OBJECTID,new IdSymbol(yytext(), yytext().length(), 0)); }
					case -95:
						break;
					case 104:
						{return new Symbol(TokenConstants.OBJECTID,new IdSymbol(yytext(), yytext().length(), 0)); }
					case -96:
						break;
					case 105:
						{return new Symbol(TokenConstants.OBJECTID,new IdSymbol(yytext(), yytext().length(), 0)); }
					case -97:
						break;
					case 106:
						{return new Symbol(TokenConstants.OBJECTID,new IdSymbol(yytext(), yytext().length(), 0)); }
					case -98:
						break;
					case 107:
						{return new Symbol(TokenConstants.OBJECTID,new IdSymbol(yytext(), yytext().length(), 0)); }
					case -99:
						break;
					case 108:
						{return new Symbol(TokenConstants.TYPEID, new IdSymbol(yytext(), yytext().length(), 0));}
					case -100:
						break;
					case 109:
						{return new Symbol(TokenConstants.OBJECTID,new IdSymbol(yytext(), yytext().length(), 0)); }
					case -101:
						break;
					case 110:
						{return new Symbol(TokenConstants.OBJECTID,new IdSymbol(yytext(), yytext().length(), 0)); }
					case -102:
						break;
					case 111:
						{return new Symbol(TokenConstants.OBJECTID,new IdSymbol(yytext(), yytext().length(), 0)); }
					case -103:
						break;
					case 112:
						{return new Symbol(TokenConstants.OBJECTID,new IdSymbol(yytext(), yytext().length(), 0)); }
					case -104:
						break;
					case 113:
						{return new Symbol(TokenConstants.TYPEID, new IdSymbol(yytext(), yytext().length(), 0));}
					case -105:
						break;
					case 114:
						{return new Symbol(TokenConstants.OBJECTID,new IdSymbol(yytext(), yytext().length(), 0)); }
					case -106:
						break;
					case 115:
						{return new Symbol(TokenConstants.OBJECTID,new IdSymbol(yytext(), yytext().length(), 0)); }
					case -107:
						break;
					case 116:
						{return new Symbol(TokenConstants.TYPEID, new IdSymbol(yytext(), yytext().length(), 0));}
					case -108:
						break;
					case 117:
						{return new Symbol(TokenConstants.OBJECTID,new IdSymbol(yytext(), yytext().length(), 0)); }
					case -109:
						break;
					case 118:
						{return new Symbol(TokenConstants.TYPEID, new IdSymbol(yytext(), yytext().length(), 0));}
					case -110:
						break;
					case 119:
						{return new Symbol(TokenConstants.OBJECTID,new IdSymbol(yytext(), yytext().length(), 0)); }
					case -111:
						break;
					case 120:
						{return new Symbol(TokenConstants.OBJECTID,new IdSymbol(yytext(), yytext().length(), 0)); }
					case -112:
						break;
					case 121:
						{return new Symbol(TokenConstants.OBJECTID,new IdSymbol(yytext(), yytext().length(), 0)); }
					case -113:
						break;
					case 122:
						{return new Symbol(TokenConstants.OBJECTID,new IdSymbol(yytext(), yytext().length(), 0)); }
					case -114:
						break;
					case 123:
						{return new Symbol(TokenConstants.OBJECTID,new IdSymbol(yytext(), yytext().length(), 0)); }
					case -115:
						break;
					case 124:
						{return new Symbol(TokenConstants.TYPEID, new IdSymbol(yytext(), yytext().length(), 0));}
					case -116:
						break;
					case 125:
						{return new Symbol(TokenConstants.TYPEID, new IdSymbol(yytext(), yytext().length(), 0));}
					case -117:
						break;
					case 126:
						{return new Symbol(TokenConstants.TYPEID, new IdSymbol(yytext(), yytext().length(), 0));}
					case -118:
						break;
					case 127:
						{return new Symbol(TokenConstants.TYPEID, new IdSymbol(yytext(), yytext().length(), 0));}
					case -119:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
						yy_mark_end();
					}
				}
			}
		}
	}
}
