package com.tts.zhanwai.utils;

import org.springframework.stereotype.Component;

@Component
public class PatternUtils {
	static enum SymbolType {
		SPACE("//s"), UNSPACT("//S"), NUMBER("[0-9]"), UNNUMBER("[^0-9]"), ALPHABET("[a-z]"), UNALPHABET(
				"[^a-z]"), CHARACTER("//w"), UNCHARACTER("//W"),;
		public String patternSymbol;

		private SymbolType(String patternSymbol) {
			this.patternSymbol = patternSymbol;
		}

		public String getPatternSymbol() {
			return patternSymbol;
		}
	}

	class PatternBuilder {
		public StringBuilder sb;

		public PatternBuilder() {
			// TODO Auto-generated constructor stub
			sb = new StringBuilder();
		}

		public String build() {
			return sb.toString();
		}

		public PatternBuilder addCommonString(String str) {
			sb.append(str);
			return this;
		}

		public PatternBuilder addStartSymbol() {
			sb.append("^");
			return this;
		}

		public PatternBuilder addEndSymbol() {
			sb.append("$");
			return this;
		}

		/**
		 * NUMBER{1,3}
		 * 
		 * @param numlimt
		 *            至少个数
		 * @param nummax
		 *            至多个数
		 * @param maxAny
		 *            至多个数是否准确{m,n}
		 * @param any
		 *            是否为任意个{0，}
		 * @return
		 */
		public PatternBuilder addSymbol(SymbolType symbolType, int nummin, int nummax, boolean maxAny, boolean any) {
			sb.append(symbolType.getPatternSymbol());
			if (any) {
				if (maxAny) {
					sb.append("{").append(nummin).append(",").append(nummax).append("}");
				} else {
					sb.append("{").append(nummin).append(",").append("}");
				}
			} else {
				sb.append("*");
			}
			return this;
		}

		/**
		 * 自定义的类型正则[1-3]{1,2}
		 * 
		 * @param symbol
		 * @param nummin
		 * @param nummax
		 * @param maxAny
		 * @param any
		 * @return
		 */
		public PatternBuilder addSymbol(String symbol, int nummin, int nummax, boolean maxAny, boolean any) {
			sb.append(symbol);
			if (any) {
				if (maxAny) {
					sb.append("{").append(nummin).append(",").append(nummax).append("}");
				} else {
					sb.append("{").append(nummin).append(",").append("}");
				}
			} else {
				sb.append("*");
			}
			return this;
		}

		/**
		 * ([1-9]|[a-z])
		 * 
		 * @param pattern1
		 * @param pattern2
		 * @return
		 */
		public PatternBuilder addPatternOr(String pattern1, String pattern2) {
			sb.append("(").append(pattern1).append("|").append(")");
			return this;
		}

		/**
		 * " \ { [ $等特殊字符转义
		 * 
		 * @param c
		 * @return
		 */
		public PatternBuilder addSpecialSymol(char c) {
			switch (c) {
			case '\\':
				sb.append("\\\\");
				break;
			default:
				sb.append("\\").append(c);
				break;
			}
			return this;
		}
	}

	public PatternBuilder builder() {
		return new PatternBuilder();
	}
}
