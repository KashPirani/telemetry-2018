//DBC version:
int DBCVersion = 1;
char gitCommit[] = "0df2a9a";
//VCU_Beaglebone can headder
#include "VCU_Beaglebone_can.h"
#include "CRC_CALC.h"
__weak int sendCanMessage(int id,int length,void *data);
// Incoming variables
// Outgoing variables

// PRO_CAN 

int init_can_driver(){
	generate_CRC_lookup_table();
	return 0;
}


int parseCANData(int id, void * data) {
	switch(id) {
	}
	return(0);
}
__weak void configCANFilters(CAN_HandleTypeDef* canHandle)
{
	CAN_FilterConfTypeDef  sFilterConfig;
	sFilterConfig.FilterNumber = 0;
	sFilterConfig.FilterMode = CAN_FILTERMODE_IDMASK;
	sFilterConfig.FilterScale = CAN_FILTERSCALE_32BIT;
	sFilterConfig.FilterIdHigh = 0x0000;
	sFilterConfig.FilterIdLow = 0x0000 + ((CAN_NODE_ADDRESS)<<8);
	sFilterConfig.FilterMaskIdHigh = 0x0000;
	sFilterConfig.FilterMaskIdLow = 0xFF00;
	sFilterConfig.FilterFIFOAssignment = 0;
	sFilterConfig.FilterActivation = ENABLE;
	sFilterConfig.BankNumber = 0;

	if(HAL_CAN_ConfigFilter(canHandle, &sFilterConfig) != HAL_OK)
	{
	  Error_Handler();
	}

	sFilterConfig.FilterIdLow = 0x0000 + (255<<8);
	sFilterConfig.FilterFIFOAssignment = 1;
	sFilterConfig.BankNumber = 1;

	if(HAL_CAN_ConfigFilter(canHandle, &sFilterConfig) != HAL_OK)
	{
	  Error_Handler();
	}
}
