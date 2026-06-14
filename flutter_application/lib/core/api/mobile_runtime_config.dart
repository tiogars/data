class MobileRuntimeConfig {
  const MobileRuntimeConfig({
    required this.gatewayBaseUrl,
    this.jwtToken,
  });

  final String gatewayBaseUrl;
  final String? jwtToken;

  MobileRuntimeConfig copyWith({
    String? gatewayBaseUrl,
    String? jwtToken,
  }) {
    return MobileRuntimeConfig(
      gatewayBaseUrl: gatewayBaseUrl ?? this.gatewayBaseUrl,
      jwtToken: jwtToken ?? this.jwtToken,
    );
  }

  static const MobileRuntimeConfig local = MobileRuntimeConfig(
    gatewayBaseUrl: 'https://gw.data.tiogars.fr',
  );
}
