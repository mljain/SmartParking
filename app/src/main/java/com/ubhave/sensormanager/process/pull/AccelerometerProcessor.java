package com.ubhave.sensormanager.process.pull;

import android.content.Context;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.pull.AbstractMotionData;
import com.ubhave.sensormanager.data.pull.AccelerometerData;

import java.util.ArrayList;

public class AccelerometerProcessor extends AbstractMotionProcessor
{
	public AccelerometerProcessor(final Context c, boolean rw, boolean sp)
	{
		super(c, rw, sp);
	}

	@Override
	protected AbstractMotionData getInstance(long pullSenseStartTimestamp, SensorConfig sensorConfig)
	{
		return new AccelerometerData(pullSenseStartTimestamp, sensorConfig);
	}

	@Override
	protected void processData(ArrayList<float[]> sensorReadings, ArrayList<Long> sensorReadingTimestamps, AbstractMotionData data)
	{
		// Future: feature extraction
	}
}
