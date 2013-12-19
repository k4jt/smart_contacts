package ua.kpi.sc.clustering;

import net.sf.javaml.clustering.Clusterer;
import net.sf.javaml.clustering.KMeans;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import ua.kpi.sc.model.IOStatistics;
import ua.kpi.sc.model.PersonStatistics;

public class ClusteringUtils {

    /**
     * @throws java.io.IOException
     */
    public static void clustereIt(HashMap<String, IOStatistics> lstSms) throws IOException {

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
        Clusterer km = new KMeans();
        /*
         * Cluster the data, it will be returned as an array of data sets, with
         * each dataset representing a cluster.
         */
        Dataset[] clusteredContactsGroups = km.cluster(data);

        int clusterNum = 0;
        for (Dataset contactGroup : clusteredContactsGroups) {
            System.out.println("========================== CLASTER " + (clusterNum++) + " ========================== ");
            for (Instance contact : contactGroup) {
                System.out.println("== CONTACT " + contact.classValue());
                for (Double score : contact) {
                    System.out.printf("%.3f ", score);
                }
                System.out.println();
            }
        }
    }

    private static Instance getFeatureVector(Map.Entry contact) {
        String id = (String) contact.getKey();
        PersonStatistics stat = (PersonStatistics) contact.getValue();
        double callScore1 = 0;
        double callScore2 = 0;
        double smsScore1 = 3 * stat.sentSmsCount + stat.inboxSmsCount; //weighted total count
        double smsScore2 = 3 * stat.sentTotalSymbols + stat.inboxTotalSymbols; //weighted total symbols
        double[] values = new double[]{callScore1, callScore2, smsScore1, smsScore2};
        Instance featureVector = new DenseInstance(values, id);
        return featureVector;
    }
}
