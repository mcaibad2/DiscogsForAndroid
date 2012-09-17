package com.discogs.utils;

import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * IntentIntegrator for the V4 Android compatibility package.
 */
public final class IntentIntegratorSupportV4 extends IntentIntegrator 
{
	private final Fragment fragment;

	/**
	 * @param fragment Fragment to handle activity response.
	 */
	public IntentIntegratorSupportV4(Fragment fragment) 
	{
		super(fragment.getActivity());
		this.fragment = fragment;
	}

	@Override
	protected void startActivityForResult(Intent intent, int code) 
	{
		fragment.startActivityForResult(intent, code);
	}
}