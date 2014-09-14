package com.yarkhs.ldi.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * Classe utilitária para manipulção de datas.
 * 
 * @author Calandra Soluções.
 */
public class DateUtil {

	/** Constante LOCALE_BRASIL. */
	public static final Locale LOCALE_BRASIL = new Locale("pt", "BR");


	// Converte data do tipo java.util.Date para java.sql.Date
	public static java.sql.Date convertUtilToSql(java.util.Date data) {

		java.sql.Date dataSql = new java.sql.Date(data.getTime());

		return dataSql;
	}


	// Adiciona mais 1 ao ano que for passado. Ex.: 2010 - Retorna 2011
	public static java.sql.Date addAno(java.util.Date data) {

		Calendar calendario = Calendar.getInstance();
		calendario.setTime(data);
		calendario.add(Calendar.YEAR, 1);

		return convertUtilToSql(calendario.getTime());

	}


	/**
	 * Verifica se uma data é válida.
	 * 
	 * @param inDate
	 *            Um string que representa uma data.
	 * 
	 * @return true, se a data é válida.
	 */
	public static boolean isValidDate(String inDate) {

		if (StringUtils.isEmpty(inDate)) {
			return true;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		if (inDate.trim().length() != dateFormat.toPattern().length()) {
			return false;
		}
		dateFormat.setLenient(false);
		try {
			dateFormat.parse(inDate.trim()).getTime();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}


	/**
	 * Verifica se uma data é válida.
	 * 
	 * @param inDate
	 *            Um string que representa uma data.
	 * 
	 * @return true, se a data é válida.
	 */
	public static boolean isDate(String inDate) {

		Pattern p = Pattern.compile("[0-9]{2,2}/[0-9]{1,2}/[0-9]{4,4}");
		Matcher m = p.matcher(inDate);

		if (StringUtils.isBlank(inDate)) {
			return true;
		}
		if (!m.matches()) {
			return false;
		}

		String[] data = inDate.split("/");
		Integer dia = new Integer(data[0]);
		Integer mes = new Integer(data[1]);
		Integer ano = new Integer(data[2]);

		if (dia > 31) {
			return false;
		} else if (mes > 12) {
			return false;
		} else if ((mes == 4 || mes == 6 || mes == 9 || mes == 11) && (dia > 30)) {
			return false;
		} else if (mes == 2) {

			if ((dia > 28) && (ano % 4 != 0)) {
				return false;
			}
			if ((dia > 29) && (ano % 4 == 0)) {
				return false;
			}
		} else if (mes == 0 || dia == 0 || ano == 0) {
			return false;
		} else if (ano < 1900) {
			return false;
		}

		return true;
	}


	/**
	 * Verifica se uma data é válida.
	 * 
	 * @param inDate
	 *            Uma data.
	 * 
	 * @return true, se a data é válida.
	 */
	public static boolean isValidDate(Date inDate) {
		if (inDate != null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

			String dateStr = dateFormat.format(inDate);

			if (dateStr.trim().length() != dateFormat.toPattern().length()) {
				return false;
			}
			dateFormat.setLenient(false);
			try {
				dateFormat.parse(dateStr.trim());
			} catch (Exception e) {
				return false;
			}
		}
		return true;
	}


	/**
	 * Verifica se uma data é válida.
	 * 
	 * @param inDate
	 *            Uma data.
	 * 
	 * @return true, se a data é válida.
	 */
	public static boolean isValidDate(java.sql.Date inDate) {
		if (inDate != null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

			String dateStr = dateFormat.format(inDate);

			if (dateStr.trim().length() != dateFormat.toPattern().length()) {
				return false;
			}
			dateFormat.setLenient(false);
			try {
				dateFormat.parse(dateStr.trim());
			} catch (Exception e) {
				return false;
			}
		}
		return true;
	}


	/**
	 * Compara duas data e retorna a menor.
	 * 
	 * @param date
	 *            Uma data (Objeto java.util.Date)
	 * @param otherDate
	 *            Uma outra data (Objeto java.util.Date)
	 * 
	 * @return a menor data.
	 */
	public static Date min(Date date, Date otherDate) {
		return date.getTime() < otherDate.getTime() ? date : otherDate;
	}


	/**
	 * Compara duas data e retorna a maior.
	 * 
	 * @param date
	 *            Uma data (Objeto java.util.Date)
	 * @param otherDate
	 *            Uma outra data (Objeto java.util.Date)
	 * 
	 * @return a maior data.
	 */
	public static Date max(Date date, Date otherDate) {
		return date.getTime() > otherDate.getTime() ? date : otherDate;
	}


	/**
	 * Verifica se uma data é válida.
	 * 
	 * @param dia
	 *            um dia
	 * @param mes
	 *            um mes
	 * @param ano
	 *            um ano
	 * 
	 * @return true, se a data é válida.
	 */
	public static boolean isValid(int dia, int mes, int ano) {
		try {
			return isValid("d/M/yyyy", dia + "/" + mes + "/" + ano);
		} catch (Exception e) {
			return false;
		}
	}


	/**
	 * Verifica se a data é valida.
	 * 
	 * @param formato
	 *            Um string com o formato, aceito por SimpleDateFormat (p.ex
	 *            "dd/MM/yyyy")
	 * @param data
	 *            Um string com a data.
	 * 
	 * @return true, se data válida.
	 * 
	 * @throws ParseException
	 *             uma exceção lançada por falha no parse da data, pelo
	 *             SimpleDateFormat.
	 */
	public static boolean isValid(String formato, String data) {

		if (formato == null || data == null) {
			return false;
		}

		try {
			SimpleDateFormat sdf = new SimpleDateFormat(formato, new Locale("pt", "BR"));
			sdf.format(sdf.parse(data)).equals(data);
		} catch (Exception pe) {
			return false;
		}

		return true;
	}


	/**
	 * Recupera o total de dias do mes.
	 * 
	 * @param ano
	 *            um ano
	 * @param mes
	 *            um mês
	 * 
	 * @return um total de dias do mês.
	 */
	public static int getTotalDiasMes(int ano, int mes) {
		GregorianCalendar novaData = new GregorianCalendar(ano, mes, 1);
		novaData.add(GregorianCalendar.DATE, -1);
		return novaData.get(GregorianCalendar.DATE);
	}


	/**
	 * Recupera um dia da semana.
	 * 
	 * @param ano
	 *            um ano
	 * @param mes
	 *            um mês
	 * @param dia
	 *            um dia
	 * 
	 * @return um dia da semana
	 */
	public static String getDiaSemana(int ano, int mes, int dia) {
		GregorianCalendar novaData = new GregorianCalendar(LOCALE_BRASIL);
		novaData.set(ano, mes, dia);
		SimpleDateFormat sdf = new SimpleDateFormat("E", LOCALE_BRASIL);
		return sdf.format(novaData);
	}


	/**
	 * Recupera o nome do mês corrente.
	 * 
	 * @param mes
	 *            um mês
	 * 
	 * @return o nome do mês corrente
	 */
	public static String getNomeMesCorrente(int mes) {
		GregorianCalendar novaData = new GregorianCalendar(LOCALE_BRASIL);
		novaData.set(2006, mes - 1, 01);
		SimpleDateFormat sdf = new SimpleDateFormat("MMMM", LOCALE_BRASIL);
		return sdf.format(novaData.getTime());
	}


	/**
	 * Formata uma variavel do tipo String para um objeto do tipo Date.
	 * 
	 * @param data
	 *            string com a data a ser formatada.
	 * 
	 * @return Date objeto criado a partir da string.
	 * 
	 * @throws ParseException
	 *             uma exceção lançada por falha no parse da data, pelo
	 *             SimpleDateFormat.
	 */
	public static Date stringToDate(String data) throws ParseException {
		Date result = null;
		if (!StringUtils.isEmpty(data)) {
			result = new SimpleDateFormat("dd/MM/yyyy", LOCALE_BRASIL).parse(data);
		}
		return result;
	}


	/**
	 * Formata uma variavel do tipo String para um objeto do tipo Date.
	 * 
	 * @param formato
	 *            um formato de data
	 * @param data
	 *            uma data
	 * 
	 * @return Date objeto criado a partir da string.
	 * 
	 * @throws ParseException
	 *             uma exceção lançada por falha no parse da data, pelo
	 *             SimpleDateFormat.
	 */
	public static Date stringToDate(String formato, String data) throws ParseException {
		Date result = null;
		if (!StringUtils.isEmpty(data)) {
			result = new SimpleDateFormat(formato, LOCALE_BRASIL).parse(data);
		}
		return result;
	}


	/**
	 * Formata uma variavel do tipo date para uma String formato dd/MM/yyyy.
	 * 
	 * @param data
	 *            uma data
	 * 
	 * @return String data formatada.
	 */
	public static String dateToString(Date data) {
		String result = null;
		if (data != null) {
			result = new SimpleDateFormat("dd/MM/yyyy", LOCALE_BRASIL).format(data);
		}
		return result;
	}


	/**
	 * Formata uma variável do tipo date para uma String formato dd-MMM-yyyy.
	 * 
	 * @param data
	 *            uma data
	 * 
	 * @return String data formatada.
	 */
	public static String dateToStringSQL(Date data) {
		String result = "";
		if (data != null) {
			result = new SimpleDateFormat("dd-MMM-yy", LOCALE_BRASIL).format(data);
		}
		return result;
	}


	/**
	 * Formata uma variável do tipo date para uma String formato dd/MM/yyyy �s
	 * HH/mm.
	 * 
	 * @param data
	 *            uma data
	 * 
	 * @return String data formatada.
	 */
	public static String dateToStringWithHour(Date data) {
		String result = null;
		if (data != null) {
			result = new SimpleDateFormat("dd/MM/yyyy HH:mm", LOCALE_BRASIL).format(data);
		}
		return result;
	}


	/**
	 * Formata uma variável do tipo date para uma String formato HH:mm.
	 * 
	 * @param data
	 *            uma data
	 * 
	 * @return String data formatada.
	 */
	public static String dateToStringOnlyHour(Date data) {
		String result = null;
		if (data != null) {
			result = new SimpleDateFormat("HH:mm", LOCALE_BRASIL).format(data);
		}
		return result;
	}


	/**
	 * Formata uma variável do tipo date para uma String formato dd/MM/yyyy.
	 * 
	 * @param data
	 *            uma data
	 * 
	 * @return String data formatada.
	 */
	public static String getDiaDate(Date data) {
		String result = "";
		if (data != null) {
			result = new SimpleDateFormat("dd", LOCALE_BRASIL).format(data);
		}
		return result;
	}


	/**
	 * Formata uma variável do tipo date para uma String formato dd/MM/yyyy.
	 * 
	 * @param data
	 *            uma data
	 * 
	 * @return String data formatada.
	 */
	public static String getMesDate(Date data) {
		String result = "";
		if (data != null) {
			result = new SimpleDateFormat("MM", LOCALE_BRASIL).format(data);
		}
		return result;
	}


	/**
	 * Formata uma variavel do tipo date para uma String formato dd/MM/yyyy.
	 * 
	 * @param data
	 *            the data
	 * 
	 * @return String data formatada.
	 */
	public static String getAnoDate(Date data) {
		String result = "";
		if (data != null) {
			result = new SimpleDateFormat("yyyy", LOCALE_BRASIL).format(data);
		}
		return result;
	}


	/**
	 * Método para comparar as das e retornar o número de dias de diferença entre
	 * elas
	 * 
	 * 
	 * @param dataLow
	 *            a data menor
	 * @param dataHigh
	 *            a data maior
	 * 
	 * @return int número de dias.
	 */
	public static int dataDiff(java.util.Date dataLow, java.util.Date dataHigh) {

		GregorianCalendar startTime = new GregorianCalendar();
		GregorianCalendar endTime = new GregorianCalendar();

		GregorianCalendar curTime = new GregorianCalendar();
		GregorianCalendar baseTime = new GregorianCalendar();

		startTime.setTime(dataLow);
		endTime.setTime(dataHigh);

		int dif_multiplier = 1;

		// Verifica a ordem de inicio das datas
		if (dataLow.compareTo(dataHigh) < 0) {
			baseTime.setTime(dataHigh);
			curTime.setTime(dataLow);
			dif_multiplier = 1;
		} else {
			baseTime.setTime(dataLow);
			curTime.setTime(dataHigh);
			dif_multiplier = -1;
		}

		int result_years = 0;
		int result_months = 0;
		int result_days = 0;

		// Para cada mes e ano, vai de mes em mes pegar o ultimo dia para import
		// acumulando
		// no total de dias. Ja leva em consideracao ano bissesto
		while ((curTime.get(GregorianCalendar.YEAR) < baseTime.get(GregorianCalendar.YEAR)) || (curTime.get(GregorianCalendar.MONTH) < baseTime.get(GregorianCalendar.MONTH))) {

			int max_day = curTime.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
			result_months += max_day;
			curTime.add(GregorianCalendar.MONTH, 1);

		}

		// Marca que � um saldo negativo ou positivo
		result_months = result_months * dif_multiplier;

		// Retorna a diferenca de dias do total dos meses
		result_days += (endTime.get(GregorianCalendar.DAY_OF_MONTH) - startTime.get(GregorianCalendar.DAY_OF_MONTH));

		return result_years + result_months + result_days;
	}


	/**
	 * Adiciona dias a um objeto Date.
	 * 
	 * @param date
	 *            um date
	 * @param dias
	 *            os dias.
	 * 
	 * @return um date
	 */
	public static Date addDias(Date date, int dias) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, dias);

		return calendar.getTime();

	}


	/**
	 * Adiciona minutos a um objeto Date.
	 * 
	 * @param date
	 *            um date
	 * @param minutos
	 *            os minutos.
	 * 
	 * @return um date
	 */
	public static Date addMinutos(Date date, int minutos) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, minutos);
		return calendar.getTime();

	}


	/**
	 * Recupera a data atual.
	 * 
	 * @return a data atual
	 */
	public static Date getDataAtual() {
		GregorianCalendar novaData = new GregorianCalendar();
		return novaData.getTime();
	}


	/**
	 * Seta a hora para meia-noite
	 */
	public static Date setHoraParaMeiaNoite(Date date) {
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar.getTime();
	}

}
