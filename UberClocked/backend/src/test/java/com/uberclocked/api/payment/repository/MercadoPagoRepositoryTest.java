package com.uberclocked.api.payment.repository;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MercadoPagoRepositoryTest {

  @Mock private PreferenceClient preferenceClient;
  @Mock private PaymentClient paymentClient;

  private MercadoPagoRepository repository;

  @BeforeEach
  void setUp() {
    repository = new MercadoPagoRepository(preferenceClient, paymentClient);
  }

  @Test
  void createPayment_success() throws Exception {
    PaymentCreateRequest request = PaymentCreateRequest.builder().build();
    Payment payment = mock(Payment.class);

    when(paymentClient.create(request)).thenReturn(payment);

    Payment result = repository.createPayment(request);

    assertNotNull(result);
  }

  @Test
  void createPayment_whenMPException_throwsRuntimeException() throws Exception {
    PaymentCreateRequest request = PaymentCreateRequest.builder().build();

    when(paymentClient.create(request)).thenThrow(new MPException("SDK error"));

    assertThrows(RuntimeException.class, () -> repository.createPayment(request));
  }

  @Test
  void createPreference_success() throws Exception {
    PreferenceRequest request = PreferenceRequest.builder().build();
    Preference preference = mock(Preference.class);

    when(preferenceClient.create(request)).thenReturn(preference);

    Preference result = repository.createPreference(request);

    assertNotNull(result);
  }

  @Test
  void createPreference_whenException_throwsRuntimeException() throws Exception {
    PreferenceRequest request = PreferenceRequest.builder().build();

    when(preferenceClient.create(request)).thenThrow(new MPException("SDK error"));

    assertThrows(RuntimeException.class, () -> repository.createPreference(request));
  }
}
