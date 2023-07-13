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
import java.util.function.Function;
import java.util.stream.Collectors;

public class StatsUtil {
  private static final Integer ONE_DAY_HOURS = 24;
  private static final Integer ONE_DAY_HOURS_FROM_ZERO = 23;
/**
   * Get total QSO's by Hour
   *
   * @param qsos List<Qso>
   * @return ObservableList<XYChart.Series < String, Integer>>
   * @deprecated
   */

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
  public <T> ObservableList<XYChart.Series<String, Integer>> getTotalsByParameter(List<Qso> qsos, Function<Qso, T> groupByFunction) {
    Map<T, List<Qso>> byParam = qsos.stream().collect(Collectors.groupingBy(groupByFunction));
    List<XYChart.Series<String, Integer>> seriesList = new ArrayList<>();
    byParam.forEach((param, l) -> {
      XYChart.Series<String, Integer> series = new XYChart.Series<>();
      series.getData().add(new XYChart.Data<>(param.toString(), l.size()));
      series.setName(param.toString());
      seriesList.add(series);
    });

    // Sort the seriesList by total in ascending order
    seriesList.sort(Comparator.comparingInt(series -> series.getData().get(0).getYValue()));

    // Convert the sorted list to ObservableList
    return FXCollections.observableArrayList(seriesList);
  }

  /**
   * Get totals by two parameters.
   *
   * @param qsos                  List<Qso>
   * @param groupByFunction       Function<Qso, T> to group by first parameter
   * @param secondGroupByFunction Function<Qso, U> to group by second parameter
   * @return ObservableList<XYChart.Series < String, Integer>>
   */
  public <T, U> ObservableList<XYChart.Series<String, Integer>> getTotalsByTwoParameters(
          List<Qso> qsos,
          Function<Qso, T> groupByFunction,
          Function<Qso, U> secondGroupByFunction) {

    Map<T, Map<U, List<Qso>>> byParams = qsos.stream()
            .collect(Collectors.groupingBy(groupByFunction, Collectors.groupingBy(secondGroupByFunction)));
    AtomicReference<XYChart.Series<String, Integer>> XySeries = new AtomicReference<>();
    ObservableList<XYChart.Series<String, Integer>> seriesList = FXCollections.observableArrayList();
    byParams.forEach((firstParam, innerMap) -> {
      Optional<XYChart.Series<String, Integer>> series = seriesList.stream()
              .filter(s -> s.getName().equalsIgnoreCase(firstParam.toString())).findFirst();
      if (series.isPresent()) {
        XySeries.set(series.get());
      } else {
        XySeries.set(new XYChart.Series<>());
        XySeries.get().setName(firstParam.toString());
      }
      innerMap.forEach((secondParam, qsoList) -> {
        XySeries.get().getData().add(new XYChart.Data<>(secondParam.toString(), qsoList.size()));
      });
      seriesList.add(XySeries.get());
    });

    // Sort the seriesList by total in ascending order
    return seriesList.sorted(Comparator.comparing(XYChart.Series::getName));


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
   * @param qsos
   * @param groupByFunction
   * @param <T>             {@link Function}
   * @return ObservableList<XYChart.Series < String, Integer>>
   */
  public <T> ObservableList<XYChart.Series<String, Integer>> getByHourAndX(List<Qso> qsos, Function<Qso, T> groupByFunction) {
    ObservableList<XYChart.Series<String, Integer>> byHourAndX = FXCollections.observableArrayList();

    Map<T, Map<LocalDate, Map<Integer, Set<Qso>>>> grouped = qsos.stream()
            .collect(Collectors.groupingBy(groupByFunction, Collectors.groupingBy(Qso::getDate,
                    Collectors.groupingBy(q -> q.getTime().getHour(), TreeMap::new, Collectors.toSet()))));

    AtomicReference<XYChart.Series<String, Integer>> byHourAndXSeries = new AtomicReference<>();
    grouped.forEach((group, dates) -> {
      AtomicInteger day = new AtomicInteger(1);
      Map<LocalDate, Map<Integer, Set<Qso>>> sortedTreeMap = new TreeMap<>(dates);
      Optional<XYChart.Series<String, Integer>> series = byHourAndX.stream()
              .filter(s -> s.getName().equalsIgnoreCase(group.toString())).findFirst();

      if (series.isPresent()) {
        byHourAndXSeries.set(series.get());
      } else {
        byHourAndXSeries.set(new XYChart.Series<>());
        byHourAndXSeries.get().setName(group.toString());
      }
      sortedTreeMap.forEach((date, hList) -> {
        if (day.get() == 1) {
          for (int i = 0; i <= ONE_DAY_HOURS_FROM_ZERO; i++) {
            if (hList.get(i) != null) {
              byHourAndXSeries.get().getData().add(new XYChart.Data<>(String.valueOf(i), hList.get(i).size()));
            } else {
              byHourAndXSeries.get().getData().add(new XYChart.Data<>(String.valueOf(i), 0));
            }
          }
        } else {
          for (int i = 0; i <= ONE_DAY_HOURS_FROM_ZERO; i++) {
            if (hList.get(i) != null) {
              byHourAndXSeries.get().getData().add(new XYChart.Data<>(String.valueOf(i + ONE_DAY_HOURS), hList.get(i).size()));
            } else {
              byHourAndXSeries.get().getData().add(new XYChart.Data<>(String.valueOf(i + ONE_DAY_HOURS), 0));
            }
          }
        }
        day.getAndIncrement();
      });
      byHourAndX.add(byHourAndXSeries.get());
    });
    return byHourAndX;
  }


}



