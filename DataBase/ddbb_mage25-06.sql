DROP DATABASE IF EXISTS mage;
CREATE DATABASE mage;
use mage;

create table producto(
`id` int NOT NULL,
`nombre` varchar(50) NOT NULL,
`precio` float NOT NULL,
`descripcion` varchar(2000) NOT NULL,
`video` varchar(500) NOT NULL,
`fecha_salida` date NOT NULL,
`desarrolladora` varchar(50) NOT NULL,
`descuento` int NOT NULL,
`activo` bit NOT NULL,
PRIMARY KEY (`id`)
);

create table requisitosProducto(
`id` int NOT NULL,
`id_producto` int NOT NULL,
`tipo` varchar(250) NOT NULL,
`so` varchar(500) NOT NULL,
`procesador` varchar(500) NOT NULL,
`memoria` varchar(500) NOT NULL,
`gpu` varchar(500) NOT NULL,
`almacenamiento` varchar(500) NOT NULL,
PRIMARY KEY (`id`),
KEY `fk_producto_requisitos_idx` (`id_producto`),
CONSTRAINT `fk_producto_requisitos` FOREIGN KEY (`id_producto`) REFERENCES `producto` (`id`) ON UPDATE CASCADE,
CONSTRAINT uq_requisitos UNIQUE(`id_producto`, `tipo`)
);

create table categoria (
`id` int NOT NULL,
`descripcion` varchar(250) NOT NULL UNIQUE,
PRIMARY KEY (`id`)
);

create table categoriaProducto(
`id` int NOT NULL,
`id_producto` int NOT NULL,
`id_categoria` int NOT NULL,
PRIMARY KEY (`id`),
KEY `fk_producto_categoria_idx` (`id_producto`),
CONSTRAINT `fk_producto_categoria` FOREIGN KEY (`id_producto`) REFERENCES `producto` (`id`) ON UPDATE CASCADE,
KEY `fk_categoria_idx` (`id_categoria`),
CONSTRAINT `fk_categoria` FOREIGN KEY (`id_categoria`) REFERENCES `categoria` (`id`) ON UPDATE CASCADE
);

create table imagenesProducto(
`id` int NOT NULL,
`id_producto` int NOT NULL,
`imagen` longblob NOT NULL,
`tipo` int NOT NULL,
PRIMARY KEY (`id`),
KEY `fk_producto_imagen_idx` (`id_producto`),
CONSTRAINT `fk_producto_imagen` FOREIGN KEY (`id_producto`) REFERENCES `producto` (`id`) ON UPDATE CASCADE
);

create table usuario(
`id` int NOT NULL,
`username` varchar(250) NOT NULL UNIQUE,
`nombre` varchar(250) NOT NULL,
`primer_apellido` varchar(250) NOT NULL,
`segundo_apellido` varchar(250) NOT NULL,
`dni` varchar(250) NOT NULL UNIQUE,
`email` varchar(250) NOT NULL UNIQUE,
`password` varchar(250) NOT NULL,
`saldo`  float DEFAULT NULL,
`imagen` longblob DEFAULT NULL,
PRIMARY KEY (`id`)
);

create table rol (
`id` int NOT NULL,
`descripcion` varchar(250) NOT NULL UNIQUE,
PRIMARY KEY (`id`)
);

create table rolUsuario(
`id` int NOT NULL,
`id_usuario` int NOT NULL UNIQUE,
`id_rol` int NOT NULL, 
PRIMARY KEY (`id`),
KEY `fk_usuario_rol_idx` (`id_usuario`),
CONSTRAINT `fk_usuario_rol` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id`) ON UPDATE CASCADE  ON DELETE CASCADE,
KEY `fk_rol_idx` (`id_rol`),
CONSTRAINT `fk_rol` FOREIGN KEY (`id_rol`) REFERENCES `rol` (`id`) ON UPDATE CASCADE
);

create table tipoTicket(
`id` int NOT NULL,
`descripcion` varchar(250) NOT NULL UNIQUE,
PRIMARY KEY (`id`)
);

create table ticketUsuario(
`id` int NOT NULL,
`id_usuario` int NOT NULL,
`id_ticket` int NOT NULL,
`mensaje` varchar(250),
`estado` varchar(250),
`fecha_creacion` DATE NOT NULL,
PRIMARY KEY (`id`),
KEY `fk_usuario_ticket_idx` (`id_usuario`),
CONSTRAINT `fk_usuario_ticket` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id`) ON UPDATE CASCADE,
KEY `fk_ticket_idx` (`id_ticket`),
CONSTRAINT `fk_ticket` FOREIGN KEY (`id_ticket`) REFERENCES `tipoTicket` (`id`) ON UPDATE CASCADE
);

create table review(
`id` int NOT NULL,
`id_usuario` int NOT NULL,
`id_producto` int NOT NULL,
`mensaje` varchar(250),
`fecha_creacion` DATE NOT NULL,
`valoracion` bit NOT NULL,
PRIMARY KEY (`id`),
KEY `fk_usuario_review_idx` (`id_usuario`),
CONSTRAINT `fk_usuario_review` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id`) ON UPDATE CASCADE,
KEY `fk_producto_review_idx` (`id_producto`),
CONSTRAINT `fk_producto_review` FOREIGN KEY (`id_producto`) REFERENCES `producto` (`id`) ON UPDATE CASCADE,
CONSTRAINT uq_review UNIQUE(`id_usuario`, `id_producto`)
);

create table lineaPedido(
`id` int NOT NULL,
`id_producto` int NOT NULL,
`precio_compra` float NOT NULL,
PRIMARY KEY (`id`),
KEY `fk_producto_pedido_idx` (`id_producto`),
CONSTRAINT `fk_producto_pedido` FOREIGN KEY (`id_producto`) REFERENCES `producto` (`id`) ON UPDATE CASCADE
);

create table pedido(
`id` int NOT NULL,
`id_usuario` int NOT NULL,
`id_linea_pedido` int NOT NULL,
`precio_compra_pedido` float NOT NULL,
`fecha` date NOT NULL,
`codigo` varchar(10) NOT NULL,
PRIMARY KEY (`id`),
KEY `fk_usuario_pedido_idx` (`id_usuario`),
CONSTRAINT `fk_usuario_pedido` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id`) ON UPDATE CASCADE,
KEY `fk_linea_pedido_idx` (`id_linea_pedido`),
CONSTRAINT `fk_linea_pedido` FOREIGN KEY (`id_linea_pedido`) REFERENCES `lineaPedido` (`id`) ON UPDATE CASCADE
);

create table lineaCesta(
`id` int NOT NULL,
`id_producto` int NOT NULL,
`precio_compra` int NOT NULL,
PRIMARY KEY (`id`),
KEY `fk_producto_cesta_idx` (`id_producto`),
CONSTRAINT `fk_producto_cesta` FOREIGN KEY (`id_producto`) REFERENCES `producto` (`id`) ON UPDATE CASCADE
);

create table cesta(
`id` int NOT NULL,
`id_usuario` int NOT NULL,
`id_linea_cesta` int NOT NULL,
PRIMARY KEY (`id`),
KEY `fk_usuario_cesta_idx` (`id_usuario`),
CONSTRAINT `fk_usuario_cesta` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id`) ON UPDATE CASCADE,
KEY `fk_linea_cesta_idx` (`id_linea_cesta`),
CONSTRAINT `fk_linea_cesta` FOREIGN KEY (`id_linea_cesta`) REFERENCES `lineaCesta` (`id`) ON UPDATE CASCADE
);