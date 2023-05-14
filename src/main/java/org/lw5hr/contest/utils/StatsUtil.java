package org.lw5hr.contest.utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import org.lw5hr.contest.charts.BarChartHelper;
import org.lw5hr.contest.model.Qso;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class StatsUtil {
  private static final Integer ONE_DAY_HOURS = 24;
  private static final Integer ONE_DAY_HOURS_FROM_ZERO = 23;

@Deprecated
  public Map<LocalDate, List<BarChartHelper>> getRatesByHour(List<Qso> qsos) {
    final Map<LocalDate, Map<Integer, List<Qso>>> grouped = qsos.stream()
            .collect(Collectors.groupingBy(Qso::getDate, Collectors.groupingBy(a -> a.getTime().getHour())));

    List<BarChartHelper> topRatesHelper = new ArrayList<>();
    grouped.forEach((g, i) -> {
      i.forEach((q, z) -> {
        BarChartHelper trh = new BarChartHelper();
        trh.setColKey(q.toString());
        trh.setValue(z.size());
        trh.setRowKey(q.toString());
        trh.setDate(g);
        topRatesHelper.add(trh);
      });
    });
    return topRatesHelper.stream()
            .collect(Collectors.groupingBy(BarChartHelper::date));
  }

  /**
   * Get total QSO's by Operator
   *
   * @param qsos List<Qso>
   * @deprecated
   */
  public void totalQsoByHourAndOperator(List<Qso> qsos) {
    Map<Object, Map<Object, Map<Object, List<Qso>>>> totalByHour = qsos.stream()
            .collect(Collectors.groupingBy(Qso::getDate, Collectors.groupingBy(q -> q.getTime().getHour(), Collectors.groupingBy(Qso::getOperator))));
    totalByHour.forEach((day, hourOperatorList) -> {
      System.out.print(System.getProperty("line.separator"));
      System.out.print("Day " + day);
      System.out.print(System.getProperty("line.separator"));
      hourOperatorList.forEach((Hour, operatorsQso) -> {
        System.out.print(" HOUR " + Hour);
        System.out.print(System.getProperty("line.separator"));
        operatorsQso.forEach((operator, qso) -> {
          System.out.print(" OPERATOR " + operator + " = " + qso.size());
          System.out.print(System.getProperty("line.separator"));
        });
      });
      System.out.print(System.getProperty("line.separator"));
    });
  }

  /**
   * Get total QSO's by Operator
   *
   * @param qsos List<Qso>
   * @return ObservableList<XYChart.Series < String, Integer>>
   */
  public ObservableList<XYChart.Series<String, Integer>> getTotalsByOp(List<Qso> qsos) {
    ObservableList<XYChart.Series<String, Integer>> byOpList = FXCollections.observableArrayList();
    Map<String, List<Qso>> byOp = qsos.stream().collect(Collectors.groupingBy(Qso::getOperator));
    byOp.forEach((op, l) -> {
      XYChart.Series<String, Integer> ByOpSeries = new XYChart.Series<>();
      ByOpSeries.getData().add(new XYChart.Data<>(op, l.size()));
      ByOpSeries.setName(op);
      byOpList.add(ByOpSeries);
    });
    return byOpList;
  }

  /**
   * Get total QSO's by Hour
   *
   * @param qsos List<Qso>
   * @return ObservableList<XYChart.Series < String, Integer>>
   */
  public ObservableList<XYChart.Series<String, Integer>> getTotalsByHour(List<Qso> qsos) {
    /* create ObservableList */
    ObservableList<XYChart.Series<String, Integer>> byHourList = FXCollections.observableArrayList();

    /* Group all QSO's by Date and Time */
    Map<LocalDate, Map<Integer, List<Qso>>> groupedByDateAndHour = qsos.stream()
            .collect(Collectors.groupingBy(Qso::getDate, Collectors.groupingBy(a -> a.getTime().getHour())));

    groupedByDateAndHour.forEach((date, map) -> {
      AtomicReference<Integer> h = new AtomicReference<>(0);
      XYChart.Series<String, Integer> byHourSeries = new XYChart.Series<>();
      byHourSeries.setName(date.toString());
      map.forEach((hour, qList) -> {
        if (Objects.equals(hour, h.get())) {
          byHourSeries.getData().add(h.get(), new XYChart.Data<>(hour.toString(), qList.size()));
        } else {
          byHourSeries.getData().add(h.get(), new XYChart.Data<>(hour.toString(), 0));
          byHourSeries.getData().add(Integer.sum(h.get(), 1), new XYChart.Data<>(String.valueOf(Integer.sum(h.get(), 1)), qList.size()));
        }
        h.set(Integer.sum(h.get(), 1));
      });
      byHourList.add(byHourSeries);
    });
    return byHourList.sorted(Comparator.comparing(XYChart.Series::getName));
  }

  /**
   * Get total QSO's by Hour and Operator
   *
   * @param qsos List<Qso>
   * @return ObservableList<XYChart.Series < String, Integer>>
   */
  public ObservableList<XYChart.Series<String, Integer>> getByHourAndOperator(List<Qso> qsos) {
    ObservableList<XYChart.Series<String, Integer>> byHourAndOperatorList = FXCollections.observableArrayList();

    Map<String, Map<LocalDate, Map<Integer, Set<Qso>>>> groupedByDateOperatorAndHour = qsos.stream()
            .collect(Collectors.groupingBy(Qso::getOperator, Collectors.groupingBy(Qso::getDate,
                    Collectors.groupingBy(q -> q.getTime().getHour(), TreeMap::new, Collectors.toSet()))));

    AtomicReference<XYChart.Series<String, Integer>> byHourOperatorSeries = new AtomicReference<>();
    groupedByDateOperatorAndHour.forEach((operator, dates) -> {
      AtomicInteger day = new AtomicInteger(1);
      Map<LocalDate, Map<Integer, Set<Qso>>> sortedTreeMap = new TreeMap<>(dates);
      Optional<XYChart.Series<String, Integer>> series = byHourAndOperatorList.stream()
              .filter(s -> s.getName().equalsIgnoreCase(operator)).findFirst();

      if (series.isPresent()) {
        byHourOperatorSeries.set(series.get());
      } else {
        byHourOperatorSeries.set(new XYChart.Series<>());
        byHourOperatorSeries.get().setName(operator);
      }
      sortedTreeMap.forEach((date, hList) -> {
        if (day.get() == 1) {
          for (int i = 0; i <= ONE_DAY_HOURS_FROM_ZERO; i++) {
            if (hList.get(i) != null) {
              byHourOperatorSeries.get().getData().add(new XYChart.Data<>(String.valueOf(i), hList.get(i).size()));
            } else {
              byHourOperatorSeries.get().getData().add(new XYChart.Data<>(String.valueOf(i), 0));
            }
          }
        } else {
          for (int i = 0; i <= ONE_DAY_HOURS_FROM_ZERO; i++) {
            if (hList.get(i) != null) {
              byHourOperatorSeries.get().getData().add(new XYChart.Data<>(String.valueOf(i + ONE_DAY_HOURS), hList.get(i).size()));
            } else {
              byHourOperatorSeries.get().getData().add(new XYChart.Data<>(String.valueOf(i + ONE_DAY_HOURS), 0));
            }
          }
        }
        day.getAndIncrement();
      });
      byHourAndOperatorList.add(byHourOperatorSeries.get());
    });
    return byHourAndOperatorList;
  }

}



