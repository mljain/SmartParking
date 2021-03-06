package com.emotionsense.demo.data.loggers;

import java.util.HashMap;

import android.content.Context;
import android.util.Log;

import com.emotionsense.demo.data.DemoApplication;
import com.ubhave.datahandler.config.DataStorageConfig;
import com.ubhave.datahandler.except.DataHandlerException;
import com.ubhave.datahandler.loggertypes.AbstractAsyncTransferLogger;
import com.ubhave.datahandler.loggertypes.AbstractDataLogger;
import com.ubhave.datahandler.transfer.DataTransfer;
import com.ubhave.sensormanager.ESException;

public class AsyncWiFiOnlyEncryptedDatabase extends AbstractAsyncTransferLogger
{
	private static AsyncWiFiOnlyEncryptedDatabase instance;

	public static AbstractDataLogger getInstance() throws ESException, DataHandlerException
	{
		if (instance == null)
		{
			instance = new AsyncWiFiOnlyEncryptedDatabase(DemoApplication.getContext());
		}
		return instance;
	}

	protected AsyncWiFiOnlyEncryptedDatabase(final Context context) throws DataHandlerException, ESException
	{
		super(context, DataStorageConfig.STORAGE_TYPE_DB);
	}

	@Override
	protected String getDataPostURL()
	{
		return RemoteServerDetails.FILE_POST_URL;
	}

	@Override
	protected String getPostKey()
	{
		return RemoteServerDetails.FILE_KEY;
	}

	@Override
	protected String getSuccessfulPostResponse()
	{
		return RemoteServerDetails.RESPONSE_ON_SUCCESS;
	}

	@Override
	protected HashMap<String, String> getPostParameters()
	{
		// Note: any additional parameters (e.g., API key-value) that your URL
		// requires
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(RemoteServerDetails.API_KEY_KEY, RemoteServerDetails.API_KEY_VALUE);
		return params;
	}

	@Override
	protected long getDataLifeMillis()
	{
		// Note: all data that is more than 1 minute old will be transferred
		return 1000L * 60 * 1;
	}

	@Override
	protected long getTransferAlarmLengthMillis()
	{
		// Note: transfer alarm will fire every 10 seconds
		Log.d(DataTransfer.TAG, "getTransferAlarmLengthMillis => "+(1000L * 10));
		return 1000L * 10;
	}
	
	@Override
	protected long getWaitForWiFiMillis()
	{
		// Note: wait for a Wi-Fi connection
		return Long.MAX_VALUE;
	}

	@Override
	protected String getFileStorageName()
	{
		// Unused for database storage
		return null;
	}

	@Override
	protected String getUniqueUserId()
	{
		// Note: this should not be a static string
		return "test-user-id";
	}

	@Override
	protected String getDeviceId()
	{
		// Note: this should not be a static string
		return "test-device-id";
	}

	@Override
	protected boolean shouldPrintLogMessages()
	{
		// Note: return false to turn off Log.d messages
		return true;
	}

	@Override
	protected String getEncryptionPassword()
	{
		// Note: return non-null password to encrypt data
		return "password";
	}
}
