package org.json.simple.parser;

import java.io.IOException;

import java.io.Reader;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class JSONParser {
	public static final int S_INIT = 0;
	public static final int S_IN_FINISHED_VALUE = 1;// string,number,boolean,null,object,array
	public static final int S_IN_OBJECT = 2;
	public static final int S_IN_ARRAY = 3;
	public static final int S_PASSED_PAIR_KEY = 4;
	public static final int S_IN_PAIR_VALUE = 5;
	public static final int S_END = 6;
	public static final int S_IN_ERROR = -1;

	private static LinkedList handlerStatusStack;
	private final static Yylex lexer = new Yylex((Reader) null);
	private static Yytoken token = null;
	private static int status = S_INIT;

	private static int peekStatus(LinkedList statusStack) {
		if (statusStack.size() == 0) {
			return -1;
		}
		final Integer status = (Integer) statusStack.getFirst();
		return status.intValue();
	}

	/**
	 * Reset the parser to the initial state without resetting the underlying
	 * reader.
	 *
	 */
	public static void reset() {
		token = null;
		status = S_INIT;
		handlerStatusStack = null;
	}

	/**
	 * Reset the parser to the initial state with a new character reader.
	 *
	 * @param in
	 *            - The new character reader.
	 * @throws IOException
	 * @throws ParseException
	 */
	public static void reset(Reader in) {
		lexer.yyreset(in);
		reset();
	}

	/**
	 * @return The position of the beginning of the current token.
	 */
	public static int getPosition() {
		return lexer.getPosition();
	}

	public Object parse(String s) throws ParseException {
		return parse(s, (ContainerFactory) null);
	}

	public Object parse(String s, ContainerFactory containerFactory) throws ParseException {
		final StringReader in = new StringReader(s);
		try {
			return parse(in, containerFactory);
		} catch (final IOException ie) {
			/*
			 * Actually it will never happen.
			 */
			throw new ParseException(-1, ParseException.ERROR_UNEXPECTED_EXCEPTION, ie);
		}
	}

	public static Object parse(Reader in) throws IOException, ParseException {
		return parse(in, (ContainerFactory) null);
	}

	/**
	 * Parse JSON text into java object from the input source.
	 *
	 * @param in
	 * @param containerFactory
	 *            - Use this factory to createyour own JSON object and JSON
	 *            array containers.
	 * @return Instance of the following: org.json.simple.JSONObject,
	 *         org.json.simple.JSONArray, java.lang.String, java.lang.Number,
	 *         java.lang.Boolean, null
	 *
	 * @throws IOException
	 * @throws ParseException
	 */
	public static Object parse(Reader in, ContainerFactory containerFactory) throws IOException, ParseException {
		reset(in);
		final LinkedList statusStack = new LinkedList();
		final LinkedList valueStack = new LinkedList();

		try {
			do {
				nextToken();
				switch (status) {
				case S_INIT:
					switch (token.type) {
					case Yytoken.TYPE_VALUE:
						status = S_IN_FINISHED_VALUE;
						statusStack.addFirst(new Integer(status));
						valueStack.addFirst(token.value);
						break;
					case Yytoken.TYPE_LEFT_BRACE:
						status = S_IN_OBJECT;
						statusStack.addFirst(new Integer(status));
						valueStack.addFirst(createObjectContainer(containerFactory));
						break;
					case Yytoken.TYPE_LEFT_SQUARE:
						status = S_IN_ARRAY;
						statusStack.addFirst(new Integer(status));
						valueStack.addFirst(createArrayContainer(containerFactory));
						break;
					default:
						status = S_IN_ERROR;
					}// inner switch
					break;

				case S_IN_FINISHED_VALUE:
					if (token.type == Yytoken.TYPE_EOF) {
						return valueStack.removeFirst();
					} else {
						throw new ParseException(getPosition(), ParseException.ERROR_UNEXPECTED_TOKEN, token);
					}

				case S_IN_OBJECT:
					switch (token.type) {
					case Yytoken.TYPE_COMMA:
						break;
					case Yytoken.TYPE_VALUE:
						if (token.value instanceof String) {
							final String key = (String) token.value;
							valueStack.addFirst(key);
							status = S_PASSED_PAIR_KEY;
							statusStack.addFirst(new Integer(status));
						} else {
							status = S_IN_ERROR;
						}
						break;
					case Yytoken.TYPE_RIGHT_BRACE:
						if (valueStack.size() > 1) {
							statusStack.removeFirst();
							valueStack.removeFirst();
							status = peekStatus(statusStack);
						} else {
							status = S_IN_FINISHED_VALUE;
						}
						break;
					default:
						status = S_IN_ERROR;
						break;
					}// inner switch
					break;

				case S_PASSED_PAIR_KEY:
					switch (token.type) {
					case Yytoken.TYPE_COLON:
						break;
					case Yytoken.TYPE_VALUE:
						statusStack.removeFirst();
						String key = (String) valueStack.removeFirst();
						Map parent = (Map) valueStack.getFirst();
						parent.put(key, token.value);
						status = peekStatus(statusStack);
						break;
					case Yytoken.TYPE_LEFT_SQUARE:
						statusStack.removeFirst();
						key = (String) valueStack.removeFirst();
						parent = (Map) valueStack.getFirst();
						final List newArray = createArrayContainer(containerFactory);
						parent.put(key, newArray);
						status = S_IN_ARRAY;
						statusStack.addFirst(new Integer(status));
						valueStack.addFirst(newArray);
						break;
					case Yytoken.TYPE_LEFT_BRACE:
						statusStack.removeFirst();
						key = (String) valueStack.removeFirst();
						parent = (Map) valueStack.getFirst();
						final Map newObject = createObjectContainer(containerFactory);
						parent.put(key, newObject);
						status = S_IN_OBJECT;
						statusStack.addFirst(new Integer(status));
						valueStack.addFirst(newObject);
						break;
					default:
						status = S_IN_ERROR;
					}
					break;

				case S_IN_ARRAY:
					switch (token.type) {
					case Yytoken.TYPE_COMMA:
						break;
					case Yytoken.TYPE_VALUE:
						List val = (List) valueStack.getFirst();
						val.add(token.value);
						break;
					case Yytoken.TYPE_RIGHT_SQUARE:
						if (valueStack.size() > 1) {
							statusStack.removeFirst();
							valueStack.removeFirst();
							status = peekStatus(statusStack);
						} else {
							status = S_IN_FINISHED_VALUE;
						}
						break;
					case Yytoken.TYPE_LEFT_BRACE:
						val = (List) valueStack.getFirst();
						final Map newObject = createObjectContainer(containerFactory);
						val.add(newObject);
						status = S_IN_OBJECT;
						statusStack.addFirst(new Integer(status));
						valueStack.addFirst(newObject);
						break;
					case Yytoken.TYPE_LEFT_SQUARE:
						val = (List) valueStack.getFirst();
						final List newArray = createArrayContainer(containerFactory);
						val.add(newArray);
						status = S_IN_ARRAY;
						statusStack.addFirst(new Integer(status));
						valueStack.addFirst(newArray);
						break;
					default:
						status = S_IN_ERROR;
					}// inner switch
					break;
				case S_IN_ERROR:
					throw new ParseException(getPosition(), ParseException.ERROR_UNEXPECTED_TOKEN, token);
				}// switch
				if (status == S_IN_ERROR) {
					throw new ParseException(getPosition(), ParseException.ERROR_UNEXPECTED_TOKEN, token);
				}
			} while (token.type != Yytoken.TYPE_EOF);
		} catch (final IOException ie) {
			throw ie;
		}

		throw new ParseException(getPosition(), ParseException.ERROR_UNEXPECTED_TOKEN, token);
	}

	private static void nextToken() throws ParseException, IOException {
		token = lexer.yylex();
		if (token == null) {
			token = new Yytoken(Yytoken.TYPE_EOF, null);
		}
	}

	private static Map createObjectContainer(ContainerFactory containerFactory) {
		if (containerFactory == null) {
			return new JSONObject();
		}
		final Map m = containerFactory.createObjectContainer();

		if (m == null) {
			return new JSONObject();
		}
		return m;
	}

	private static List createArrayContainer(ContainerFactory containerFactory) {
		if (containerFactory == null) {
			return new JSONArray();
		}
		final List l = containerFactory.creatArrayContainer();

		if (l == null) {
			return new JSONArray();
		}
		return l;
	}

	public void parse(String s, ContentHandler contentHandler) throws ParseException {
		parse(s, contentHandler, false);
	}

	public void parse(String s, ContentHandler contentHandler, boolean isResume) throws ParseException {
		final StringReader in = new StringReader(s);
		try {
			parse(in, contentHandler, isResume);
		} catch (final IOException ie) {
			/*
			 * Actually it will never happen.
			 */
			throw new ParseException(-1, ParseException.ERROR_UNEXPECTED_EXCEPTION, ie);
		}
	}

	public void parse(Reader in, ContentHandler contentHandler) throws IOException, ParseException {
		parse(in, contentHandler, false);
	}

	/**
	 * Stream processing of JSON text.
	 *
	 * @see ContentHandler
	 *
	 * @param in
	 * @param contentHandler
	 * @param isResume
	 *            - Indicates if it continues previous parsing operation. If set
	 *            to true, resume parsing the old stream, and parameter 'in'
	 *            will be ignored. If this method is called for the first time
	 *            in this instance, isResume will be ignored.
	 *
	 * @throws IOException
	 * @throws ParseException
	 */
	public void parse(Reader in, ContentHandler contentHandler, boolean isResume) throws IOException, ParseException {
		if (!isResume) {
			reset(in);
			handlerStatusStack = new LinkedList();
		} else {
			if (handlerStatusStack == null) {
				isResume = false;
				reset(in);
				handlerStatusStack = new LinkedList();
			}
		}

		final LinkedList statusStack = handlerStatusStack;

		try {
			do {
				switch (status) {
				case S_INIT:
					try {
						contentHandler.startJSON();
					} catch (final java.text.ParseException e4) {
						// TODO Auto-generated catch block
						e4.printStackTrace();
					}
					nextToken();
					switch (token.type) {
					case Yytoken.TYPE_VALUE:
						status = S_IN_FINISHED_VALUE;
						statusStack.addFirst(new Integer(status));
						try {
							if (!contentHandler.primitive(token.value)) {
								return;
							}
						} catch (final java.text.ParseException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						break;
					case Yytoken.TYPE_LEFT_BRACE:
						status = S_IN_OBJECT;
						statusStack.addFirst(new Integer(status));
						try {
							if (!contentHandler.startObject()) {
								return;
							}
						} catch (final java.text.ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
					case Yytoken.TYPE_LEFT_SQUARE:
						status = S_IN_ARRAY;
						statusStack.addFirst(new Integer(status));
						try {
							if (!contentHandler.startArray()) {
								return;
							}
						} catch (final java.text.ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
					default:
						status = S_IN_ERROR;
					}// inner switch
					break;

				case S_IN_FINISHED_VALUE:
					nextToken();
					if (token.type == Yytoken.TYPE_EOF) {
						try {
							contentHandler.endJSON();
						} catch (final java.text.ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						status = S_END;
						return;
					} else {
						status = S_IN_ERROR;
						throw new ParseException(getPosition(), ParseException.ERROR_UNEXPECTED_TOKEN, token);
					}

				case S_IN_OBJECT:
					nextToken();
					switch (token.type) {
					case Yytoken.TYPE_COMMA:
						break;
					case Yytoken.TYPE_VALUE:
						if (token.value instanceof String) {
							final String key = (String) token.value;
							status = S_PASSED_PAIR_KEY;
							statusStack.addFirst(new Integer(status));
							try {
								if (!contentHandler.startObjectEntry(key)) {
									return;
								}
							} catch (final java.text.ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} else {
							status = S_IN_ERROR;
						}
						break;
					case Yytoken.TYPE_RIGHT_BRACE:
						if (statusStack.size() > 1) {
							statusStack.removeFirst();
							status = peekStatus(statusStack);
						} else {
							status = S_IN_FINISHED_VALUE;
						}
						try {
							if (!contentHandler.endObject()) {
								return;
							}
						} catch (final java.text.ParseException e4) {
							// TODO Auto-generated catch block
							e4.printStackTrace();
						}
						break;
					default:
						status = S_IN_ERROR;
						break;
					}// inner switch
					break;

				case S_PASSED_PAIR_KEY:
					nextToken();
					switch (token.type) {
					case Yytoken.TYPE_COLON:
						break;
					case Yytoken.TYPE_VALUE:
						statusStack.removeFirst();
						status = peekStatus(statusStack);
						try {
							if (!contentHandler.primitive(token.value)) {
								return;
							}
						} catch (final java.text.ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						try {
							if (!contentHandler.endObjectEntry()) {
								return;
							}
						} catch (final java.text.ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
					case Yytoken.TYPE_LEFT_SQUARE:
						statusStack.removeFirst();
						statusStack.addFirst(new Integer(S_IN_PAIR_VALUE));
						status = S_IN_ARRAY;
						statusStack.addFirst(new Integer(status));
						try {
							if (!contentHandler.startArray()) {
								return;
							}
						} catch (final java.text.ParseException e5) {
							// TODO Auto-generated catch block
							e5.printStackTrace();
						}
						break;
					case Yytoken.TYPE_LEFT_BRACE:
						statusStack.removeFirst();
						statusStack.addFirst(new Integer(S_IN_PAIR_VALUE));
						status = S_IN_OBJECT;
						statusStack.addFirst(new Integer(status));
						try {
							if (!contentHandler.startObject()) {
								return;
							}
						} catch (final java.text.ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
					default:
						status = S_IN_ERROR;
					}
					break;

				case S_IN_PAIR_VALUE:
					/*
					 * S_IN_PAIR_VALUE is just a marker to indicate the end of
					 * an object entry, it doesn't proccess any token, therefore
					 * delay consuming token until next round.
					 */
					statusStack.removeFirst();
					status = peekStatus(statusStack);
					try {
						if (!contentHandler.endObjectEntry()) {
							return;
						}
					} catch (final java.text.ParseException e4) {
						// TODO Auto-generated catch block
						e4.printStackTrace();
					}
					break;

				case S_IN_ARRAY:
					nextToken();
					switch (token.type) {
					case Yytoken.TYPE_COMMA:
						break;
					case Yytoken.TYPE_VALUE:
						try {
							if (!contentHandler.primitive(token.value)) {
								return;
							}
						} catch (final java.text.ParseException e3) {
							// TODO Auto-generated catch block
							e3.printStackTrace();
						}
						break;
					case Yytoken.TYPE_RIGHT_SQUARE:
						if (statusStack.size() > 1) {
							statusStack.removeFirst();
							status = peekStatus(statusStack);
						} else {
							status = S_IN_FINISHED_VALUE;
						}
						try {
							if (!contentHandler.endArray()) {
								return;
							}
						} catch (final java.text.ParseException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
						break;
					case Yytoken.TYPE_LEFT_BRACE:
						status = S_IN_OBJECT;
						statusStack.addFirst(new Integer(status));
						try {
							if (!contentHandler.startObject()) {
								return;
							}
						} catch (final java.text.ParseException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						break;
					case Yytoken.TYPE_LEFT_SQUARE:
						status = S_IN_ARRAY;
						statusStack.addFirst(new Integer(status));
						try {
							if (!contentHandler.startArray()) {
								return;
							}
						} catch (final java.text.ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
					default:
						status = S_IN_ERROR;
					}// inner switch
					break;

				case S_END:
					return;

				case S_IN_ERROR:
					throw new ParseException(getPosition(), ParseException.ERROR_UNEXPECTED_TOKEN, token);
				}// switch
				if (status == S_IN_ERROR) {
					throw new ParseException(getPosition(), ParseException.ERROR_UNEXPECTED_TOKEN, token);
				}
			} while (token.type != Yytoken.TYPE_EOF);
		} catch (final IOException ie) {
			status = S_IN_ERROR;
			throw ie;
		} catch (final ParseException pe) {
			status = S_IN_ERROR;
			throw pe;
		} catch (final RuntimeException re) {
			status = S_IN_ERROR;
			throw re;
		} catch (final Error e) {
			status = S_IN_ERROR;
			throw e;
		}

		status = S_IN_ERROR;
		throw new ParseException(getPosition(), ParseException.ERROR_UNEXPECTED_TOKEN, token);
	}
}
