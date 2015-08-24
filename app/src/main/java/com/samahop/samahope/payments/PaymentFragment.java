package com.samahop.samahope.payments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wallet.Cart;
import com.google.android.gms.wallet.FullWallet;
import com.google.android.gms.wallet.FullWalletRequest;
import com.google.android.gms.wallet.MaskedWallet;
import com.google.android.gms.wallet.MaskedWalletRequest;
import com.google.android.gms.wallet.PaymentMethodTokenizationParameters;
import com.google.android.gms.wallet.PaymentMethodTokenizationType;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;
import com.google.android.gms.wallet.fragment.BuyButtonText;
import com.google.android.gms.wallet.fragment.Dimension;
import com.google.android.gms.wallet.fragment.SupportWalletFragment;
import com.google.android.gms.wallet.fragment.WalletFragmentInitParams;
import com.google.android.gms.wallet.fragment.WalletFragmentMode;
import com.google.android.gms.wallet.fragment.WalletFragmentOptions;
import com.google.android.gms.wallet.fragment.WalletFragmentStyle;
import com.samahop.samahope.MainActivity;
import com.samahop.samahope.R;
import com.stripe.model.Token;


public class PaymentFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener  {

    private GoogleApiClient googleApiClient;
    private int donationAmount;

    private static final int LOAD_MASKED_WALLET_REQUEST_CODE = 1000;
    private static final int LOAD_FULL_WALLET_REQUEST_CODE = 1001;

    public PaymentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_payment, container, false);

        // Inflate the layout for this fragment//set toolbar and enable back navigation
        MainActivity activity = (MainActivity) getActivity();
        activity.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        activity.getSupportActionBar().setTitle("Select Amount");
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initializePaymentUI(v);

        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Wallet.API, new Wallet.WalletOptions.Builder()
                        .setEnvironment(WalletConstants.ENVIRONMENT_SANDBOX)
                        .setTheme(WalletConstants.THEME_HOLO_LIGHT)
                        .build())
                .build();

        createAndAddWalletFragment();

        return v;
    }

    private void initializePaymentUI(View v) {
        RadioGroup amountGroup = (RadioGroup) v.findViewById(R.id.radio_group_payment);
        final EditText customText = (EditText)v.findViewById(R.id.edit_text_custom);

        amountGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.radio_10:
                        donationAmount = 10;
                        customText.setEnabled(false);
                        customText.setText("");
                        break;

                    case R.id.radio_25:
                        donationAmount = 25;
                        customText.setEnabled(false);
                        customText.setText("");
                        break;

                    case R.id.radio_50:
                        donationAmount = 50;
                        customText.setEnabled(false);
                        customText.setText("");
                        break;

                    case R.id.radio_100:
                        donationAmount = 100;
                        customText.setEnabled(false);
                        customText.setText("");
                        break;

                    case R.id.radio_custom:
                        customText.setEnabled(true);
                        break;
                }
            }
        });

        customText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void afterTextChanged(Editable s) {
                String text = customText.getText().toString();
                if (!isNumeric(text) || text.isEmpty())
                    return;

                // set the correct amount to be passed to the server
                donationAmount = Integer.valueOf(customText.getText().toString());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // retrieve the error code, if available
        int errorCode = -1;
        if (data != null) {
            errorCode = data.getIntExtra(WalletConstants.EXTRA_ERROR_CODE, -1);
        }

        RadioGroup amountGroup = (RadioGroup) getView().findViewById(R.id.radio_group_payment);
        if (amountGroup.getCheckedRadioButtonId() == -1)
        {
            // no radio buttons are checked
            Snackbar.make(getView(), "Please select an amount first!", Snackbar.LENGTH_SHORT).show();
            return;
        }

        switch (requestCode) {
            case LOAD_MASKED_WALLET_REQUEST_CODE:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        MaskedWallet maskedWallet =
                                data.getParcelableExtra(WalletConstants.EXTRA_MASKED_WALLET);

                        FullWalletRequest fullWalletRequest = FullWalletRequest.newBuilder()
                                .setCart(Cart.newBuilder()
                                        .setCurrencyCode("USD")
                                        .setTotalPrice(String.valueOf(donationAmount))
                                        .build())
                                .setGoogleTransactionId(maskedWallet.getGoogleTransactionId())
                                .build();

                        Wallet.Payments.loadFullWallet(googleApiClient, fullWalletRequest, LOAD_FULL_WALLET_REQUEST_CODE);
                        Snackbar.make(getView(), "Processing payment... ", Snackbar.LENGTH_LONG).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                    default:
                        handleError(errorCode);
                        break;
                }
                break;
            case WalletConstants.RESULT_ERROR:
                handleError(errorCode);
                break;
            case LOAD_FULL_WALLET_REQUEST_CODE:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        FullWallet fullWallet = data.getParcelableExtra(WalletConstants.EXTRA_FULL_WALLET);
                        String tokenJSON = fullWallet.getPaymentMethodToken().getToken();

                        Token token = Token.GSON.fromJson(tokenJSON, Token.class);
                        token.setAmount(donationAmount);

                        // FOR PRODUCTION: create an async task to POST the token to the
                        // Samahope web server, where it will then create a credit card charge,
                        // setup an email subscription for updates, and send name/phone number for
                        // data collection
                        Log.i("PAY_TOKEN", token.toString());


                        launchDonationCompletePage();
                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                    default:
                        handleError(errorCode);
                        break;
                }
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    private void launchDonationCompletePage() {
        Intent intent = new Intent(getActivity(), DonationCompleteActivity.class);
        getActivity().startActivity(intent);
    }

    protected void handleError(int errorCode) {
        switch (errorCode) {
            case WalletConstants.ERROR_CODE_SPENDING_LIMIT_EXCEEDED:
                Snackbar.make(getView(), "Limit exceeded", Snackbar.LENGTH_LONG).show();
                break;
            case WalletConstants.ERROR_CODE_INVALID_PARAMETERS:
            case WalletConstants.ERROR_CODE_AUTHENTICATION_FAILURE:
            case WalletConstants.ERROR_CODE_BUYER_ACCOUNT_ERROR:
            case WalletConstants.ERROR_CODE_MERCHANT_ACCOUNT_ERROR:
            case WalletConstants.ERROR_CODE_SERVICE_UNAVAILABLE:
            case WalletConstants.ERROR_CODE_UNSUPPORTED_API_VERSION:
            case WalletConstants.ERROR_CODE_UNKNOWN:
            default:
                // unrecoverable error
                Snackbar.make(getView(), "Unrecoverable error", Snackbar.LENGTH_LONG).show();
                break;
        }
    }

    private void createAndAddWalletFragment() {
        WalletFragmentStyle walletFragmentStyle = new WalletFragmentStyle()
                .setBuyButtonText(BuyButtonText.DONATE_WITH_GOOGLE)
                .setBuyButtonWidth(Dimension.MATCH_PARENT);

        WalletFragmentOptions walletFragmentOptions = WalletFragmentOptions.newBuilder()
                .setEnvironment(WalletConstants.ENVIRONMENT_SANDBOX)
                .setFragmentStyle(walletFragmentStyle)
                .setTheme(WalletConstants.THEME_HOLO_LIGHT)
                .setMode(WalletFragmentMode.BUY_BUTTON)
                .build();
        SupportWalletFragment mWalletFragment = SupportWalletFragment.newInstance(walletFragmentOptions);

        // set up stripe as the primary payment gateway
        // FOR PRODUCTION: relace test key with live key
        MaskedWalletRequest maskedWalletRequest = MaskedWalletRequest.newBuilder()

                // Request credit card tokenization with Stripe by specifying tokenization parameters:
                .setPaymentMethodTokenizationParameters(PaymentMethodTokenizationParameters.newBuilder()
                        .setPaymentMethodTokenizationType(PaymentMethodTokenizationType.PAYMENT_GATEWAY)
                        .addParameter("gateway", "stripe")
                        .addParameter("stripe:publishableKey", "pk_test_n2H2MVvno70I8CxQBTjCPEHw")
                        .addParameter("stripe:version", com.stripe.Stripe.VERSION)
                        .build())

                .setShippingAddressRequired(false)
                .setEstimatedTotalPrice(String.valueOf(donationAmount))
                .setCurrencyCode("USD")
                .setMerchantName("Samahope")
                .build();

        // Set the parameters and initialize the masked wallet request
        // if the masked request goes through, we will handle the full wallet request
        WalletFragmentInitParams initParams = WalletFragmentInitParams.newBuilder()
                .setMaskedWalletRequest(maskedWalletRequest)
                .setMaskedWalletRequestCode(LOAD_MASKED_WALLET_REQUEST_CODE)
                .build();
        mWalletFragment.initialize(initParams);

        // add "Donate with Google" button to the UI
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.dynamic_wallet_button_fragment, mWalletFragment)
                .commit();
    }

    public int getDonationAmount() { return donationAmount; }

    public void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    public void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        // don't need to do anything here
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // don't need to do anything here
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) { }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isNumeric(String str)
    {
        for (char c : str.toCharArray())
            if (!Character.isDigit(c)) return false;

        return true;
    }
}
