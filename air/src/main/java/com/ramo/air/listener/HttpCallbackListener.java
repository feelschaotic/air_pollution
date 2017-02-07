package com.ramo.air.listener;
/**
 * ����java�Ļص�����
 * ����˽ӿ� 
 * �����̵߳��ù����෽�������߳����ӷ�����ʱ�ܼ����������������¼�
 * @author guoyf
 *
 */
public interface HttpCallbackListener {
	void onSucc(String response);
	void onError(Exception e);
}
