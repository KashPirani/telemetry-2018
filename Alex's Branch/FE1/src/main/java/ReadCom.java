import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

class messageData
{
    public int idCOM;
    public long dataCOM;
}

class ReadCom implements Runnable {


    public void run() {
        Queue ids = new LinkedList();
        HashMap hm = new HashMap(150);
        try {
            BufferedReader br = new BufferedReader(new FileReader("/dev/cu.usbmodem1411")); //S0 or S1 or S2
            System.out.println(br.read() + "\n\n");
            while(true) {
                if (readSerial(ids, br))
                {
                    messageData newDat = (messageData) ids.remove();
                    Translate.translate(hm, newDat.dataCOM, newDat.idCOM);
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n");
                    System.out.println(hm);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean readSerial(Queue ids, BufferedReader br) {
        try {
            int idLength = 0, id = 0, i = 0, dataLength = 0;
            long dat = 0;
            if (ids.size()>100000) //Acceptable lag
                ids.clear();

            while(true)  //Waits until star message is received
                if (br.read() == 1)
                    break;

            idLength = br.read();
            for (i = 0; i < idLength; i ++)
                id += (br.read() << (8*(idLength-1-i)));
            messageData dU = new messageData();

            if (br.read() != 9) { // Makes  sure message completes
                System.out.println("Identifier not completed");
                return false;
            }

            dataLength = br.read();
            for (i = 0; i < dataLength; i ++)
                dat += ((long)br.read() << (8*(dataLength-1-i)));

            if (br.read() != 0) { // Makes  sure message completes
                System.out.println("Message not completed");
                return false;
            }
            dU.idCOM = id;
            dU.dataCOM = dat;
            System.out.println(id+" "+dat+"\n\n");
            ids.add(dU);
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void updateSchema (HashMap hm)
    {
        for (Map.Entry<String, Integer> entry : hm.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            FESchema.put(key, value);
        }
    }
}