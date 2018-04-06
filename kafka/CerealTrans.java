/*
// kafka imports
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.serialization.ByteArraySerializer;
// avro imports
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;*/
// java utils import
import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public static final String FESchema = "{"
        + "\"type\":\"record\","
        + "\"name\":\"FERecord\","
        + "\"fields\":["
        + "  { \"name\":\"state\", \"type\":\"int\" },"
        + "  { \"name\":\"DBC1\", \"type\":\"float\" },"
        + "  { \"name\":\"DBC2\", \"type\":\"float\" }"
        + "]}";

public class Main {

    public static void main(String[] args) {
        new Thread(new ReadThread()).start();
        System.out.println("Hello Alex!");
    }

}

class messageData
{
    public int idCOM;
    public long dataCOM;
}

class ReadThread implements Runnable {


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
                    translate(hm, newDat.dataCOM, newDat.idCOM);
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

    //switch case for the can messages
    public void translate(HashMap hm, long value, int id){
        long shift = 0;
        double temp = 0;
        switch (id) {
            case 1073741824:
                //VECTOR__INDEPENDENT_SIG_MSG
                shift = (1<<10)-1;
                temp = (value>>0)&shift;
                temp = temp*0.25;
                temp = temp+90;
                hm.put("TempInletRadBatt", temp);

                shift = (1<<10)-1;
                temp = (value>>0)&shift;
                temp = temp*0.25;
                temp = temp+90;
                hm.put("TempOutletRadBatt", temp);

                shift = (1<<10)-1;
                temp = (value>>0)&shift;
                temp = temp*0.25;
                temp = temp+90;
                hm.put("TempOutletRadMotorLeft", temp);

                shift = (1<<10)-1;
                temp = (value>>0)&shift;
                temp = temp*0.25;
                temp = temp+90;
                hm.put("TempOutletRadMotorRight", temp);

                shift = (1<<10)-1;
                temp = (value>>0)&shift;
                temp = temp*0.25;
                temp = temp+90;
                hm.put("TempInletRadMotorRight", temp);

                shift = (1<<10)-1;
                temp = (value>>0)&shift;
                temp = temp*0.25;
                temp = temp+90;
                hm.put("TempInletRadMotorLeft", temp);

                break;
            case 33:
                //ChargeCart_VERSION
                shift = (1<<8)-1;
                temp = (value>>0)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("ChargeCart_Version_DB", temp);

                shift = (1<<56)-1;
                temp = (value>>8)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("ChargeCart_Version_code", temp);

                break;
            case 32:
                //BMU_VERSION
                shift = (1<<8)-1;
                temp = (value>>0)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("BMU_Version_DB", temp);

                shift = (1<<56)-1;
                temp = (value>>8)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("BMU_Version_code", temp);

                break;
            case 31:
                //DCU_VERSION
                shift = (1<<8)-1;
                temp = (value>>0)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("DCU_Version_DB", temp);

                shift = (1<<56)-1;
                temp = (value>>8)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("DCU_Version_code", temp);

                break;
            case 30:
                //PDU_VERSION
                shift = (1<<8)-1;
                temp = (value>>0)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("PDU_Version_DB", temp);

                shift = (1<<56)-1;
                temp = (value>>8)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("PDU_Version_code", temp);

                break;
            case 29:
                //VCU_VERSION
                shift = (1<<8)-1;
                temp = (value>>0)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("VCU_Version_DB", temp);

                shift = (1<<56)-1;
                temp = (value>>8)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("VCU_Version_code", temp);

                break;
            case 28:
                //TempInverterRight
                shift = (1<<12)-1;
                temp = (value>>0)&shift;
                temp = temp*0.25;
                temp = temp+90;
                hm.put("StateInverterRight", temp);

                shift = (1<<12)-1;
                temp = (value>>12)&shift;
                temp = temp*0.25;
                temp = temp+90;
                hm.put("TempInverterDeltaRight", temp);

                shift = (1<<12)-1;
                temp = (value>>24)&shift;
                temp = temp*0.25;
                temp = temp+90;
                hm.put("TempInverterRight", temp);

                shift = (1<<12)-1;
                temp = (value>>36)&shift;
                temp = temp*0.25;
                temp = temp+90;
                hm.put("InverterAUTHSEEDRight", temp);

                shift = (1<<4)-1;
                temp = (value>>48)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("PRO_CAN_RES", temp);

                shift = (1<<4)-1;
                temp = (value>>52)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("PRO_CAN_COUNT", temp);

                shift = (1<<8)-1;
                temp = (value>>56)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("PRO_CAN_CRC", temp);

                break;
            case 27:
                //TempMotorRight
                shift = (1<<12)-1;
                temp = (value>>0)&shift;
                temp = temp*0.25;
                temp = temp+90;
                hm.put("TempMotorRight", temp);

                shift = (1<<12)-1;
                temp = (value>>12)&shift;
                temp = temp*0.25;
                temp = temp+90;
                hm.put("TempMotorDeltaRight", temp);

                shift = (1<<4)-1;
                temp = (value>>48)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("PRO_CAN_RES", temp);

                shift = (1<<4)-1;
                temp = (value>>52)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("PRO_CAN_COUNT", temp);

                shift = (1<<8)-1;
                temp = (value>>56)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("PRO_CAN_CRC", temp);

                break;
            case 26:
                //BusHVFeedbackRight
                shift = (1<<16)-1;
                temp = (value>>0)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("CurrentHVBusInverterRight", temp);

                shift = (1<<16)-1;
                temp = (value>>16)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("VoltageHVBusInverterRight", temp);

                shift = (1<<4)-1;
                temp = (value>>48)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("PRO_CAN_RES", temp);

                shift = (1<<4)-1;
                temp = (value>>52)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("PRO_CAN_COUNT", temp);

                shift = (1<<8)-1;
                temp = (value>>56)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("PRO_CAN_CRC", temp);

                break;
            case 25:
                //SpeedFeedbackRight
                shift = (1<<16)-1;
                temp = (value>>0)&shift;
                temp = temp*0.25;
                temp = temp+90;
                hm.put("SpeedMotorRight", temp);

                shift = (1<<4)-1;
                temp = (value>>48)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("PRO_CAN_RES", temp);

                shift = (1<<4)-1;
                temp = (value>>52)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("PRO_CAN_COUNT", temp);

                shift = (1<<8)-1;
                temp = (value>>56)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("PRO_CAN_CRC", temp);

                break;
            case 24:
                //TorqueFeedbackRight
                shift = (1<<16)-1;
                temp = (value>>0)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("TorqueEstimateRight", temp);

                shift = (1<<16)-1;
                temp = (value>>16)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("TorqueAvailableDriveRight", temp);

                shift = (1<<16)-1;
                temp = (value>>32)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("TorqueAvailableBrakingRight", temp);

                shift = (1<<4)-1;
                temp = (value>>48)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("PRO_CAN_COUNT", temp);

                shift = (1<<4)-1;
                temp = (value>>52)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("PRO_CAN_RES", temp);

                shift = (1<<8)-1;
                temp = (value>>56)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("PRO_CAN_CRC", temp);

                break;
            case 23:
                //VoltageLimitRight
                shift = (1<<12)-1;
                temp = (value>>0)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("VoltageLimitHighInverterRight", temp);

                shift = (1<<12)-1;
                temp = (value>>12)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("VoltageLimitLowInverterRight", temp);

                shift = (1<<4)-1;
                temp = (value>>48)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("PRO_CAN_RES", temp);

                shift = (1<<4)-1;
                temp = (value>>52)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("PRO_CAN_COUNT", temp);

                shift = (1<<8)-1;
                temp = (value>>56)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("PRO_CAN_CRC", temp);

                break;
            case 22:
                //CurrentLimitRight
                shift = (1<<12)-1;
                temp = (value>>0)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("CurrentLimitDschrgInverterRight", temp);

                shift = (1<<12)-1;
                temp = (value>>12)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("CurrentLimitChargeInverterRight", temp);

                shift = (1<<4)-1;
                temp = (value>>48)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("PRO_CAN_RES", temp);

                shift = (1<<4)-1;
                temp = (value>>52)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("PRO_CAN_COUNT", temp);

                shift = (1<<8)-1;
                temp = (value>>56)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("PRO_CAN_CRC", temp);

                break;
            case 21:
                //SpeedLimitRight
                shift = (1<<16)-1;
                temp = (value>>0)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("InverterCommandRight", temp);

                shift = (1<<16)-1;
                temp = (value>>16)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("SpeedLimitForwardRight", temp);

                shift = (1<<16)-1;
                temp = (value>>32)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("SpeedLimitReverseRight", temp);

                shift = (1<<4)-1;
                temp = (value>>48)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("PRO_CAN_RES", temp);

                shift = (1<<4)-1;
                temp = (value>>52)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("PRO_CAN_COUNT", temp);

                shift = (1<<8)-1;
                temp = (value>>56)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("PRO_CAN_CRC", temp);

                break;
            case 20:
                //TorqueLimitRight
                shift = (1<<16)-1;
                temp = (value>>0)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("TorqueDemandRight", temp);

                shift = (1<<16)-1;
                temp = (value>>16)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("TorqueLimitBrakingRight", temp);

                shift = (1<<16)-1;
                temp = (value>>32)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("TorqueLimitDriveRight", temp);

                shift = (1<<4)-1;
                temp = (value>>48)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("PRO_CAN_RES", temp);

                shift = (1<<4)-1;
                temp = (value>>52)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("PRO_CAN_COUNT", temp);

                shift = (1<<8)-1;
                temp = (value>>56)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("PRO_CAN_CRC", temp);

                break;
            case 19:
                //TempInverterLeft
                shift = (1<<12)-1;
                temp = (value>>0)&shift;
                temp = temp*0.25;
                temp = temp+90;
                hm.put("StateInverterLeft", temp);

                shift = (1<<12)-1;
                temp = (value>>12)&shift;
                temp = temp*0.25;
                temp = temp+90;
                hm.put("TempInverterDeltaLeft", temp);

                shift = (1<<12)-1;
                temp = (value>>24)&shift;
                temp = temp*0.25;
                temp = temp+90;
                hm.put("TempInverterLeft", temp);

                shift = (1<<12)-1;
                temp = (value>>36)&shift;
                temp = temp*0.25;
                temp = temp+90;
                hm.put("InverterAUTHSEEDLeft", temp);

                shift = (1<<4)-1;
                temp = (value>>48)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("PRO_CAN_RES", temp);

                shift = (1<<4)-1;
                temp = (value>>52)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("PRO_CAN_COUNT", temp);

                shift = (1<<8)-1;
                temp = (value>>56)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("PRO_CAN_CRC", temp);

                break;
            case 18:
                //TempMotorLeft
                shift = (1<<12)-1;
                temp = (value>>0)&shift;
                temp = temp*0.25;
                temp = temp+90;
                hm.put("TempMotorLeft", temp);

                shift = (1<<12)-1;
                temp = (value>>12)&shift;
                temp = temp*0.25;
                temp = temp+90;
                hm.put("TempMotorDeltaLeft", temp);

                shift = (1<<4)-1;
                temp = (value>>48)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("PRO_CAN_RES", temp);

                shift = (1<<4)-1;
                temp = (value>>52)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("PRO_CAN_COUNT", temp);

                shift = (1<<8)-1;
                temp = (value>>56)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("PRO_CAN_CRC", temp);

                break;
            case 17:
                //BusHVFeedbackLeft
                shift = (1<<16)-1;
                temp = (value>>0)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("CurrentHVBusInverterLeft", temp);

                shift = (1<<16)-1;
                temp = (value>>16)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("VoltageHVBusInverterLeft", temp);

                shift = (1<<4)-1;
                temp = (value>>48)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("PRO_CAN_RES", temp);

                shift = (1<<4)-1;
                temp = (value>>52)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("PRO_CAN_COUNT", temp);

                shift = (1<<8)-1;
                temp = (value>>56)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("PRO_CAN_CRC", temp);

                break;
            case 16:
                //SpeedFeedbackLeft
                shift = (1<<16)-1;
                temp = (value>>0)&shift;
                temp = temp*0.25;
                temp = temp+90;
                hm.put("SpeedMotorLeft", temp);

                shift = (1<<4)-1;
                temp = (value>>48)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("PRO_CAN_RES", temp);

                shift = (1<<4)-1;
                temp = (value>>52)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("PRO_CAN_COUNT", temp);

                shift = (1<<8)-1;
                temp = (value>>56)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("PRO_CAN_CRC", temp);

                break;
            case 15:
                //TorqueFeedbackLeft
                shift = (1<<16)-1;
                temp = (value>>0)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("TorqueEstimateLeft", temp);

                shift = (1<<16)-1;
                temp = (value>>16)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("TorqueAvailableDriveLeft", temp);

                shift = (1<<16)-1;
                temp = (value>>32)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("TorqueAvailableBrakingLeft", temp);

                shift = (1<<4)-1;
                temp = (value>>48)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("PRO_CAN_COUNT", temp);

                shift = (1<<4)-1;
                temp = (value>>52)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("PRO_CAN_RES", temp);

                shift = (1<<8)-1;
                temp = (value>>56)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("PRO_CAN_CRC", temp);

                break;
            case 14:
                //VoltageLimitLeft
                shift = (1<<12)-1;
                temp = (value>>0)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("VoltageLimitHighInverterLeft", temp);

                shift = (1<<12)-1;
                temp = (value>>12)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("VoltageLimitLowInverterLeft", temp);

                shift = (1<<4)-1;
                temp = (value>>48)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("PRO_CAN_RES", temp);

                shift = (1<<4)-1;
                temp = (value>>52)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("PRO_CAN_COUNT", temp);

                shift = (1<<8)-1;
                temp = (value>>56)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("PRO_CAN_CRC", temp);

                break;
            case 13:
                //CurrentLimitLeft
                shift = (1<<12)-1;
                temp = (value>>0)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("CurrentLimitDschrgInverterLeft", temp);

                shift = (1<<12)-1;
                temp = (value>>12)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("CurrentLimitChargeInverterLeft", temp);

                shift = (1<<4)-1;
                temp = (value>>48)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("PRO_CAN_RES", temp);

                shift = (1<<4)-1;
                temp = (value>>52)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("PRO_CAN_COUNT", temp);

                shift = (1<<8)-1;
                temp = (value>>56)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("PRO_CAN_CRC", temp);

                break;
            case 12:
                //SpeedLimitLeft
                shift = (1<<16)-1;
                temp = (value>>0)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("InverterCommandLeft", temp);

                shift = (1<<16)-1;
                temp = (value>>16)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("SpeedLimitForwardLeft", temp);

                shift = (1<<16)-1;
                temp = (value>>32)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("SpeedLimitReverseLeft", temp);

                shift = (1<<4)-1;
                temp = (value>>48)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("PRO_CAN_RES", temp);

                shift = (1<<4)-1;
                temp = (value>>52)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("PRO_CAN_COUNT", temp);

                shift = (1<<8)-1;
                temp = (value>>56)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("PRO_CAN_CRC", temp);

                break;
            case 11:
                //TorqueLimitLeft
                shift = (1<<16)-1;
                temp = (value>>0)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("TorqueDemandLeft", temp);

                shift = (1<<16)-1;
                temp = (value>>16)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("TorqueLimitBrakingLeft", temp);

                shift = (1<<16)-1;
                temp = (value>>32)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("TorqueLimitDriveLeft", temp);

                shift = (1<<4)-1;
                temp = (value>>48)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("PRO_CAN_RES", temp);

                shift = (1<<4)-1;
                temp = (value>>52)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("PRO_CAN_COUNT", temp);

                shift = (1<<8)-1;
                temp = (value>>56)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("PRO_CAN_CRC", temp);

                break;
            case 10:
                //WSBRR_WheelData
                shift = (1<<18)-1;
                temp = (value>>0)&shift;
                temp = temp*0.00068664812716723;
                temp = temp+60;
                hm.put("SpeedWheelRightBack", temp);

                break;
            case 9:
                //WSBRL_WheelData
                shift = (1<<18)-1;
                temp = (value>>0)&shift;
                temp = temp*0.00068664812716723;
                temp = temp+60;
                hm.put("SpeedWheelLeftBack", temp);

                break;
            case 8:
                //PDU_StateBatteryLV
                shift = (1<<14)-1;
                temp = (value>>0)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("voltageBatteryLV", temp);

                break;
            case 7:
                //BMU_stateBatteryHV
                shift = (1<<20)-1;
                temp = (value>>0)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("voltageBatteryHV", temp);

                shift = (1<<22)-1;
                temp = (value>>20)&shift;
                temp = temp*0.001;
                temp = temp+0;
                hm.put("currentDCBatteryHV", temp);

                shift = (1<<22)-1;
                temp = (value>>42)&shift;
                temp = temp*0.01;
                temp = temp+0;
                hm.put("powerBatteryHV", temp);

                break;
            case 6:
                //PDU_ChannelStatus
                shift = (1<<4)-1;
                temp = (value>>0)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("statusPowerBMU", temp);

                shift = (1<<4)-1;
                temp = (value>>4)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("statusPowerCoolingFanBattery", temp);

                shift = (1<<4)-1;
                temp = (value>>8)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("statusPowerCoolingFanLeft", temp);

                shift = (1<<4)-1;
                temp = (value>>12)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("statusPowerCoolingFanRight", temp);

                shift = (1<<4)-1;
                temp = (value>>16)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("statusPowerCoolingPumpBattery", temp);

                shift = (1<<4)-1;
                temp = (value>>20)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("statusPowerCoolingPumpLeft", temp);

                shift = (1<<4)-1;
                temp = (value>>24)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("statusPowerCoolingPumpRight", temp);

                shift = (1<<4)-1;
                temp = (value>>28)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("statusPowerDAU", temp);

                shift = (1<<4)-1;
                temp = (value>>32)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("statusPowerDCU", temp);

                shift = (1<<4)-1;
                temp = (value>>36)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("statusPowerIMD", temp);

                shift = (1<<4)-1;
                temp = (value>>40)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("statusPowerMCLeft", temp);

                shift = (1<<4)-1;
                temp = (value>>44)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("statusPowerMCRight", temp);

                shift = (1<<4)-1;
                temp = (value>>48)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("statusPowerVCU", temp);

                break;
            case 5:
                //PDU_batteryStatusLV
                shift = (1<<10)-1;
                temp = (value>>0)&shift;
                temp = temp*0.1;
                temp = temp+0;
                hm.put("stateBatteryChargeLV", temp);

                shift = (1<<10)-1;
                temp = (value>>10)&shift;
                temp = temp*0.1;
                temp = temp+0;
                hm.put("stateBatteryHealthLV", temp);

                shift = (1<<10)-1;
                temp = (value>>20)&shift;
                temp = temp*0.1;
                temp = temp+0;
                hm.put("stateBatteryPowerLV", temp);

                shift = (1<<14)-1;
                temp = (value>>30)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("voltageBatteryLV", temp);

                shift = (1<<14)-1;
                temp = (value>>44)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("voltageBusLV", temp);

                break;
            case 4:
                //BMU_batteryStatusHV
                shift = (1<<10)-1;
                temp = (value>>0)&shift;
                temp = temp*0.1;
                temp = temp+0;
                hm.put("stateBatteryChargeHV", temp);

                shift = (1<<10)-1;
                temp = (value>>10)&shift;
                temp = temp*0.1;
                temp = temp+0;
                hm.put("stateBatteryHealthHV", temp);

                shift = (1<<12)-1;
                temp = (value>>20)&shift;
                temp = temp*0.1;
                temp = temp+0;
                hm.put("stateBatteryPowerHV", temp);

                shift = (1<<10)-1;
                temp = (value>>32)&shift;
                temp = temp*0.25;
                temp = temp+90;
                hm.put("TempCellMax", temp);

                shift = (1<<10)-1;
                temp = (value>>42)&shift;
                temp = temp*0.25;
                temp = temp+90;
                hm.put("TempCellMin", temp);

                shift = (1<<4)-1;
                temp = (value>>52)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("stateBMS", temp);

                break;
            case 3:
                //BMU_stateBusHV
                shift = (1<<22)-1;
                temp = (value>>0)&shift;
                temp = temp*0.1;
                temp = temp+0;
                hm.put("voltageBusHV", temp);

                shift = (1<<3)-1;
                temp = (value>>24)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("stateContactorNegative", temp);

                shift = (1<<3)-1;
                temp = (value>>27)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("stateContactorPositive", temp);

                shift = (1<<16)-1;
                temp = (value>>32)&shift;
                temp = temp*0.1;
                temp = temp+0;
                hm.put("voltageCellMax", temp);

                shift = (1<<16)-1;
                temp = (value>>48)&shift;
                temp = temp*0.1;
                temp = temp+0;
                hm.put("voltageCellMin", temp);

                break;
            case 2:
                //PDU_DCDC_Status
                shift = (1<<12)-1;
                temp = (value>>0)&shift;
                temp = temp*0.01220703125;
                temp = temp+0;
                hm.put("currentOutputDCDC", temp);

                shift = (1<<4)-1;
                temp = (value>>12)&shift;
                temp = temp*1;
                temp = temp+0;
                hm.put("Status_DCDC", temp);

                break;
            case 1:
                //WSBFL_WheelData
                shift = (1<<18)-1;
                temp = (value>>0)&shift;
                temp = temp*0.00068664812716723;
                temp = temp+60;
                hm.put("SpeedWheelLeftFront", temp);

                break;
            case 0:
                //WSBFR_WheelData
                shift = (1<<18)-1;
                temp = (value>>0)&shift;
                temp = temp*0.00068664812716723;
                temp = temp+60;
                hm.put("SpeedWheelRightFront", temp);

                break;
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
