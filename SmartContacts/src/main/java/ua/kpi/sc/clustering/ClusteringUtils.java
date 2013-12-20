package ua.kpi.sc.clustering;

import net.sf.javaml.clustering.Clusterer;
import net.sf.javaml.clustering.MultiKMeans;
import net.sf.javaml.clustering.evaluation.ClusterEvaluation;
import net.sf.javaml.clustering.evaluation.SumOfSquaredErrors;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.DistanceMeasure;
import net.sf.javaml.distance.EuclideanDistance;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import ua.kpi.sc.model.PersonStatistics;

public class ClusteringUtils {

    /**
     *
     * @param lstSms
     * @throws java.io.IOException
     */
    public static HashMap<String, String> clustereIt(Map<String, PersonStatistics> lstSms) throws IOException {

        /* Make dataset */
        Dataset data = new DefaultDataset();
        for (Map.Entry contact : lstSms.entrySet()) {

            /* next commented line do the same but maybe in more suitable way */
            Instance featureVector = getFeatureVector(contact);
            data.add(featureVector);
        }

        /*
         * Create a new instance of the KMeans algorithm, with no options
         * specified. By default this will generate 4 clusters.
         */
        int clusters = 4;
        int iterations = 1000;
        int repeats = 10;
        DistanceMeasure dm = new EuclideanDistance();
        ClusterEvaluation sse = new SumOfSquaredErrors();
        Clusterer km = new MultiKMeans(clusters, iterations, repeats, dm, sse);
        /*
         * Cluster the data, it will be returned as an array of data sets, with
         * each dataset representing a cluster.
         */
        Dataset[] clusteredContactsGroups = km.cluster(data);

        int clusterNum = 0;

        TreeMap<Double, Integer> metricToClusterMap = new TreeMap<Double, Integer>();
        for (Dataset contactGroup : clusteredContactsGroups) {
            System.out.println("========================== CLASTER " + clusterNum + " ========================== ");
            double allVectorSum = 0;
            for (Instance contact : contactGroup) {
                System.out.println("== CONTACT " + contact.classValue());
                for (Double score : contact) {
                    allVectorSum += score;
                    System.out.printf("%.3f ", score);
                }
                System.out.println();
            }
            metricToClusterMap.put(allVectorSum / contactGroup.size(), clusterNum);
            clusterNum++;
        }

        int i = 0;
        HashMap<Integer, String> clusterToNameMap = new HashMap<Integer, String>();
        for (Map.Entry entry : metricToClusterMap.entrySet()) {
            Integer value = (Integer) entry.getValue();
            switch (i) {
                case 0:
                    clusterToNameMap.put(value, "25% of love");
                    break;
                case 1:
                    clusterToNameMap.put(value, "50% of love");
                    break;
                case 2:
                    clusterToNameMap.put(value, "75% of love");
                    break;
                case 3:
                    clusterToNameMap.put(value, "100% of love");
                    break;
            }
            i++;
        }

        HashMap<String, String> contactToNameMap = new HashMap<String, String>();

        clusterNum = 0;
        for (Dataset contactGroup : clusteredContactsGroups) {
            for (Instance contact : contactGroup) {
                contactToNameMap.put((String) contact.classValue(), clusterToNameMap.get(clusterNum));
            }
            clusterNum++;
        }

//        for (Map.Entry entry : contactToNameMap.entrySet()) {
//            System.out.println(entry.getKey() + " " +entry.getValue());
//        }

        return contactToNameMap;
    }


    private static Instance getFeatureVector(Map.Entry contact) {
        String id = (String) contact.getKey();
        PersonStatistics stat = (PersonStatistics) contact.getValue();
        double callScore1 = 3 * stat.outgoingCallsCount + stat.incomingCallsCount;
        double callScore2 = 3 * stat.outgoingTotalDuration + stat.incomingTotalDuration;
        double smsScore1 = 3 * stat.sentSmsCount + stat.inboxSmsCount; //weighted total count
        double smsScore2 = 3 * stat.sentTotalSymbols + stat.inboxTotalSymbols; //weighted total symbols
        double[] values = new double[]{callScore1, callScore2, smsScore1, smsScore2};
        Instance featureVector = new DenseInstance(values, id);
        return featureVector;
    }
}
