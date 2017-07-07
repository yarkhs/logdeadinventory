package com.yarkhs.ldi.util;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

public class Util {

	//	static final Logger log = Logger.getLogger(Util.class);

	static {
		Locale l = new Locale("pt", "BR");
		Locale.setDefault(l);
	}


//	public static String criptografar(String str) throws IOException {
//		String strCriptografada = Base64Coder.encodeString(str);
//		return URLEncoder.encode(strCriptografada, "ISO-8859-1");
//	}
//
//
//	public static String descriptografar(String str) throws IOException {
//		String strDescriptografada = URLDecoder.decode(str, "ISO-8859-1");
//		return Base64Coder.decodeString(strDescriptografada);
//	}


	/**
	 * Função do método: <br>
	 * - Remover caracteres especiais e colocar espaço em branco no lugar dos mesmos; <br>
	 * Ex: "Paulo Jose C." = "Paulo Jose C" <br>
	 * - Remover espaços em branco no início, no fim, e em excesso; <br>
	 * Ex: " Paulo  Jose  C " = "Paulo Jose C" <br>
	 * - Colocar a string em maiúsculo. <br>
	 * Ex: "Paulo Jose C" = "PAULO JOSE C" <br>
	 * - Remover acentuação das letras. <br>
	 * Ex: "Paulo José C" = "Paulo Jose C" <br>
	 */
	public static String padronizaString(String nomeAntigo) {

		if (Util.empty(nomeAntigo))
			return "";

		StringBuffer nomeNovo = new StringBuffer();
		int charAscii = 0;
		nomeAntigo = nomeAntigo.toUpperCase();

		for (int i = 0; i < nomeAntigo.length(); i++) {

			charAscii = nomeAntigo.charAt(i);

			//Remove acentuação da letra A
			if (charAscii >= 192 && charAscii <= 197)
				nomeNovo.append("A");

			//Remove acentuação da letra E
			else if (charAscii >= 200 && charAscii <= 203)
				nomeNovo.append("E");

			//Remove acentuação da letra I
			else if (charAscii >= 204 && charAscii <= 207)
				nomeNovo.append("I");

			//Remove acentuação da letra O
			else if (charAscii >= 210 && charAscii <= 214)
				nomeNovo.append("O");

			//Remove acentuação da letra U
			else if (charAscii >= 217 && charAscii <= 220)
				nomeNovo.append("U");

			//Remove acentuação da letra C
			else if (charAscii == 199)
				nomeNovo.append("C");

			//Remove acentuação da letra N
			else if (charAscii == 209)
				nomeNovo.append("N");

			//Remove acentuação da letra Y
			else if (charAscii == 159 || charAscii == 221)
				nomeNovo.append("Y");

			//Acrescenta char caso seja número ou letra
			else if ((charAscii >= 48 && charAscii <= 57) || (charAscii >= 65 && charAscii <= 90))
				nomeNovo.append((char) charAscii);

			else if (i < nomeAntigo.length() - 1)
				//Acrescenta char caso ele seja espaço e caso o próximo não seja
				if (charAscii == 32 && nomeAntigo.charAt(i + 1) != 32)
					nomeNovo.append((char) charAscii);

				//Acrescenta espaço caso não tenha espaço no próximo char
				else if (nomeAntigo.charAt(i + 1) != 32)
					nomeNovo.append((char) 32);

		}

		//Remove espaço no início da string
		if (nomeNovo.charAt(0) == 32)
			nomeNovo.deleteCharAt(0);

		//Remove espaço no final da string
		if (nomeNovo.charAt(nomeNovo.length() - 1) == 32)
			nomeNovo.deleteCharAt(nomeNovo.length() - 1);

		return nomeNovo.toString();
	}


	public static String dateToStr(Date data) {

		if (empty(data))
			return "";

		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		return format.format(data);

	}


	public static String bigDecimalToStr(BigDecimal valor) {

		if (valor == null)
			return null;

		NumberFormat form = DecimalFormat.getInstance();
		form.setMinimumFractionDigits(valor.scale());
		form.setMaximumFractionDigits(valor.scale());
		form.setGroupingUsed(false);
		return form.format(valor);

	}


	public static BigDecimal strToBigDecimal(String valor) {

		if (valor == null)
			return null; // so vai funcionar para o Brasil

		StringBuffer s = new StringBuffer(valor);
		int pos;
		if (s.indexOf(",") >= 0) {
			while ((pos = s.indexOf(".")) >= 0) {
				s.deleteCharAt(pos);
			}
		}
		valor = s.toString().replace(',', '.');
		return new BigDecimal(valor);

	}


	public static String dataAtualExtenso() {

		DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, new Locale("pt", "br"));
		return df.format(new Date());

	}


	public static String dataAnterior() {

		Calendar calendar = Calendar.getInstance(new Locale("pt", "BR"));
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		calendar.add(Calendar.DATE, -1);
		return sdf.format(calendar.getTime()).toString();

	}


	public static String getPrimeiroDiaMes() {

		Calendar calendar = Calendar.getInstance(new Locale("pt", "BR"));
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return sdf.format(calendar.getTime()).toString();

	}


	public static String getUltimoDiaMes() {

		Calendar calendar = Calendar.getInstance(new Locale("pt", "BR"));
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		calendar.add(Calendar.MONTH, +1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		return sdf.format(calendar.getTime()).toString();

	}


	public static String getPrimeiroDiaMesAnterior() {

		Calendar calendar = Calendar.getInstance(new Locale("pt", "BR"));
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return sdf.format(calendar.getTime()).toString();

	}


	public static String getUltimoDiaMesAnterior() {

		Calendar calendar = Calendar.getInstance(new Locale("pt", "BR"));
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		return sdf.format(calendar.getTime()).toString();

	}


	public static String dataAtual() {

		Calendar calendar = Calendar.getInstance(new Locale("pt", "BR"));
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		return sdf.format(calendar.getTime()).toString();

	}


	public static String horaAtual() {

		Calendar calendar = Calendar.getInstance(new Locale("pt", "BR"));
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
		return sdf.format(calendar.getTime()).toString();

	}


	/**
	 * Obs: Caso seja Integer ou BigDecimal, não testa se é 0, pois é considerado um valor válido.
	 * 
	 * @param Object
	 * @return <b>true</> se for vazio.
	 */
	public static Boolean empty(Object o) {

		if (o == null) {
			return true;
		}

		if (o instanceof String) {
			return StringUtils.isBlank((String) o);
		}

		if (o instanceof Boolean) {
			return !((Boolean) o).booleanValue();
		}

		//		if ( o instanceof Collection ) {
		//			return CollectionUtils.isEmpty((Collection) o);
		//		}

		if (o.getClass().isArray()) {
			return ArrayUtils.isEmpty((Object[]) o);
		}
		return false;
	}


	public static Boolean empty(Integer[] intValues) {
		if (ArrayUtils.isEmpty(intValues)) {
			return true;
		}

		for (Integer intValue : intValues) {
			if (Util.empty(intValue)) {
				return true;
			}
		}

		if (intValues.length == 1 && intValues[0].equals(0)) {
			return true;
		}

		return false;
	}


	public static Boolean empty(String[] strValues) {
		if (ArrayUtils.isEmpty(strValues)) {
			return true;
		}

		for (String strValue : strValues) {
			if (Util.empty(strValue)) {
				return true;
			}
		}

		return false;
	}


	public static int getIdade(Date nascimento) {

		if (nascimento.compareTo(new Date()) > 0)
			return 0;

		Calendar ini = Calendar.getInstance(new Locale("pt", "BR"));
		Calendar fim = Calendar.getInstance(new Locale("pt", "BR"));

		ini.setTime(nascimento);
		fim.setTime(new Date());

		fim.add(Calendar.YEAR, -ini.get(Calendar.YEAR));
		fim.add(Calendar.MONTH, -ini.get(Calendar.MONTH));

		if (fim.get(Calendar.DATE) <= ini.get(Calendar.DATE) - 1 && fim.get(Calendar.YEAR) == 1)
			return 0;

		fim.add(Calendar.DATE, -ini.get(Calendar.DATE) + 1);
		return fim.get(Calendar.YEAR);

	}


	public static int getIdade(Date nascimento, Date dataBase) {

		Calendar ini = Calendar.getInstance(new Locale("pt", "BR"));
		Calendar fim = Calendar.getInstance(new Locale("pt", "BR"));

		ini.setTime(nascimento);
		fim.setTime(dataBase);

		fim.add(Calendar.YEAR, -ini.get(Calendar.YEAR));
		fim.add(Calendar.MONTH, -ini.get(Calendar.MONTH));
		if (fim.get(Calendar.DATE) <= ini.get(Calendar.DATE) - 1 && fim.get(Calendar.YEAR) == 1)
			return 0;

		fim.add(Calendar.DATE, -ini.get(Calendar.DATE) + 1);
		return fim.get(Calendar.YEAR);

	}


	public static int getQtdDiasEntreDatas(Date dtInicio, Date dtFim) {
		return Integer.parseInt((dtFim.getTime() - dtInicio.getTime()) / (1000 * 60 * 60 * 24) + "");
	}


	public static String getDateAsString(Date data, String formato) {

		SimpleDateFormat format = new SimpleDateFormat(formato);
		return format.format(data);

	}


	public static String mascaraStringComoData(String ddmmyyyy) {

		if (empty(ddmmyyyy))
			ddmmyyyy = "";

		if (empty(ddmmyyyy) || ddmmyyyy.indexOf("/") > 0)
			return ddmmyyyy;
		else
			return ddmmyyyy.substring(0, 2) + "/" + ddmmyyyy.substring(2, 4) + "/" + ddmmyyyy.substring(4, 8);

	}


	public static String mascaraStringComoHora(String hhmm) {

		if (hhmm.length() == 3)
			hhmm = "0" + hhmm;

		return hhmm.substring(0, 2) + ":" + hhmm.substring(2, 4);

	}


	public static Date getDataRetroativa(Date date, Integer numDias) {

		Calendar calendar = Calendar.getInstance(new Locale("pt", "BR"));
		calendar.setTime(date);
		calendar.add(Calendar.DATE, numDias * -1);

		return calendar.getTime();

	}


	/**
	 * 
	 * @param valor
	 * @return CNPJ no formato 99.999.999/9999-99
	 *         Se o tamanho do valor passado for diferente de 14 \n
	 *         o valor não será formatado.
	 * 
	 */
	public static String formatStrToCNPJ(String valor) {

		if (valor.trim().length() != 14)
			return valor;

		String aux = valor;

		String str = aux.substring(0, 2).concat(".").concat(aux.substring(2, 5)).concat(".").concat(aux.substring(5, 8)).concat("/").concat(aux.substring(8, 12)).concat("-")
				.concat(aux.substring(12, 14));

		return str;

	}


	public static Boolean validaCpf(String cpf) {
		if (cpf.length() != 11)
			return false;

		int d1, d2;
		int digito1, digito2, resto;
		int digitoCPF;
		String nDigResult;
		d1 = d2 = 0;
		digito1 = digito2 = resto = 0;
		for (int n_Count = 1; n_Count < cpf.length() - 1; n_Count++) {
			digitoCPF = Integer.valueOf(cpf.substring(n_Count - 1, n_Count)).intValue();
			d1 = d1 + (11 - n_Count) * digitoCPF;
			d2 = d2 + (12 - n_Count) * digitoCPF;
		}

		resto = (d1 % 11);

		if (resto < 2)
			digito1 = 0;
		else
			digito1 = 11 - resto;

		d2 += 2 * digito1;

		resto = (d2 % 11);

		if (resto < 2)
			digito2 = 0;
		else
			digito2 = 11 - resto;

		String nDigVerific = cpf.substring(cpf.length() - 2, cpf.length());
		nDigResult = String.valueOf(digito1) + String.valueOf(digito2);

		return nDigVerific.equals(nDigResult);
	}


	public static Boolean validaCns(String cns) {
		if (cns.trim().length() != 15) {
			return (false);
		}

		float soma;
		float resto, dv;
		String pis = new String("");
		String resultado = new String("");
		pis = cns.substring(0, 11);

		soma = ((Integer.valueOf(pis.substring(0, 1)).intValue()) * 15) + ((Integer.valueOf(pis.substring(1, 2)).intValue()) * 14) + ((Integer.valueOf(pis.substring(2, 3)).intValue()) * 13)
				+ ((Integer.valueOf(pis.substring(3, 4)).intValue()) * 12) + ((Integer.valueOf(pis.substring(4, 5)).intValue()) * 11) + ((Integer.valueOf(pis.substring(5, 6)).intValue()) * 10)
				+ ((Integer.valueOf(pis.substring(6, 7)).intValue()) * 9) + ((Integer.valueOf(pis.substring(7, 8)).intValue()) * 8) + ((Integer.valueOf(pis.substring(8, 9)).intValue()) * 7)
				+ ((Integer.valueOf(pis.substring(9, 10)).intValue()) * 6) + ((Integer.valueOf(pis.substring(10, 11)).intValue()) * 5);

		resto = soma % 11;
		dv = 11 - resto;

		if (dv == 11) {
			dv = 0;
		}

		if (dv == 10) {
			soma = ((Integer.valueOf(pis.substring(0, 1)).intValue()) * 15) + ((Integer.valueOf(pis.substring(1, 2)).intValue()) * 14) + ((Integer.valueOf(pis.substring(2, 3)).intValue()) * 13)
					+ ((Integer.valueOf(pis.substring(3, 4)).intValue()) * 12) + ((Integer.valueOf(pis.substring(4, 5)).intValue()) * 11) + ((Integer.valueOf(pis.substring(5, 6)).intValue()) * 10)
					+ ((Integer.valueOf(pis.substring(6, 7)).intValue()) * 9) + ((Integer.valueOf(pis.substring(7, 8)).intValue()) * 8) + ((Integer.valueOf(pis.substring(8, 9)).intValue()) * 7)
					+ ((Integer.valueOf(pis.substring(9, 10)).intValue()) * 6) + ((Integer.valueOf(pis.substring(10, 11)).intValue()) * 5) + 2;

			resto = soma % 11;
			dv = 11 - resto;
			resultado = pis + "001" + String.valueOf((int) dv);
		} else {
			resultado = pis + "000" + String.valueOf((int) dv);
		}

		if (!cns.equals(resultado)) {
			return (false);
		} else {
			return (true);
		}
	}

}