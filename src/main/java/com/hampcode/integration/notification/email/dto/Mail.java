package com.hampcode.integration.notification.email.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mail {
        private String from;   // Remitente del correo
        private String to;     // Destinatario del correo
        private String subject; // Asunto del correo
        private Map<String, Object> model; // Modelo de datos para la plantilla
}
