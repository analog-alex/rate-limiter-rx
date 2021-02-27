import { FastifyInstance } from "fastify";
import { ingressController } from "./controllers/ingressController";

export default async function router(fastify: FastifyInstance) {
  fastify.register(ingressController, { prefix: "/api/v1/" });
}
